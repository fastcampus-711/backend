package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common.reaction.service.CountOfReactionAndCommentApplyService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Primary
@Service
@Transactional
public class CommonPostService<E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {
    private final CommonPostRepository<E> commonPostRepository;
    private final CountOfReactionAndCommentApplyService<E> countOfReactionAndCommentApplyService;

    public CommonPostService(CommonPostRepository<E> commonPostRepository) {
        this.countOfReactionAndCommentApplyService = new CountOfReactionAndCommentApplyService<>(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

    public S getPost(long postId) {
        //Logic: 조회수 +1
        E commonPost = commonPostRepository.findByComments_CommonPostId(postId)
                .orElse(
                        commonPostRepository.findById(postId)
                                .orElseThrow(InvalidTableIdException::new)
                );

        commonPost.plusHits();

        //Logic: 댓글 수 갱신
        commonPost.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(commonPost.getComments())
        );

        return (S) commonPost.toResponseDtoWithComments();
    }

    public List<S> getPostListByCategoryId(Long categoryId, HttpServletRequest request, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        String dtype = getDtype(request);
        List<E> list;
        if (categoryId == null) {
            list = commonPostRepository.findByDtype(dtype, pageable).getContent();

            return list.stream()
                    .map(e -> (S) e.toResponseDtoWithoutComments())
                    .toList();
        } else {
            list = commonPostRepository.findByCategoryId(categoryId);

            return list.stream()
                    .map(e -> (S) e.toResponseDtoWithoutComments())
                    .toList();
        }
    }

    public List<S> getPostList(HttpServletRequest request, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost"))
            list = commonPostRepository.findAll(pageable).getContent();
        else
            list = commonPostRepository.findByDtype(dtype, pageable).getContent();

        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
    }

    public List<S> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost"))
            list = commonPostRepository.findByTitleContainingIgnoreCaseAndVisible(keyword, pageable, true).getContent();
        else
            list = commonPostRepository.findByTitleContainingIgnoreCaseAndDtypeAndVisible(keyword, dtype, pageable, true).getContent();

        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
    }

    public S createPost(Q requestDto) {
        E entity = (E) requestDto.toEntity().setUserId();
        commonPostRepository.save(entity);

        return (S) entity.toResponseDtoWithoutComments();
    }

    public S updatePost(HttpServletRequest request, long postId, Q requestDto) {
        E entity = validUpdateOrDeleteRequestIsPossible(request, postId);

        return (S) entity.updateByUpdateRequest(requestDto)
                .toResponseDtoWithoutComments();
    }

    public long deletePost(HttpServletRequest request, long postId) {
        validUpdateOrDeleteRequestIsPossible(request, postId);

        commonPostRepository.deleteById(postId);

        return postId;
    }

    private static String getDtype(HttpServletRequest request) {
        String[] URIs = request.getRequestURI()
                .split("/");
        String target = URIs.length <= 2 ? "" : URIs[2];

        return Arrays.stream(CategoryCode.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseGet(() -> CategoryCode.공통)
                .getDtype();
    }

    private E validUpdateOrDeleteRequestIsPossible(HttpServletRequest request, long postId) {
        E entity = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        if (!entity.validUpdateOrDeleteAuthority())
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);

        if (!entity.checkIsDtypeIsEquals(getDtype(request)))
            throw new PostException(ErrorCode.INCORRECT_TARGET_BOARD);

        return entity;
    }
}
