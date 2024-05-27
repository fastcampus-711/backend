package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common.reaction.service.CountCommentsAndReactionApplyService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Primary
@Service
@Transactional
public class CommonPostService<E extends CommonPost,
        T extends CommonPostDto,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {
    private final CommonPostRepository<E> commonPostRepository;
    private final CountCommentsAndReactionApplyService<E> countOfReactionAndCommentApplyService;

    public CommonPostService(CommonPostRepository<E> commonPostRepository) {
        this.countOfReactionAndCommentApplyService = new CountCommentsAndReactionApplyService<>(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

//    public Page<S> searchPost(BoardGroup boardGroup, Long categoryId, String keyword, Pageable pageable) {
//        if (keyword == null || keyword.isBlank()) {
//            return getPostListByCategoryId(boardGroup, categoryId, pageable)
//        }
//
//    }

    /**
     * 게시판 + 분류 검색
     *  자유게시판 : 인기게시글
     */
    public List<S> getPostListByCategoryId(BoardGroup boardGroup, Long categoryId, Pageable pageable) {

        List<E> list;
        if (categoryId == 0) {
            // 게시판 조회
            if (BoardGroup.FREES.equals(boardGroup)) {
                if (pageable.getPageNumber() == 1) {
                    // 인기 게시글
                }
            }

            list = commonPostRepository.findByDtype(boardGroup.getTable(), pageable).getContent();
        } else {
            // 게시판 + 카테고리 조회
            list = commonPostRepository.findByDtypeAndCategoryId(boardGroup.getTable(), categoryId, pageable).getContent();
        }
        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
    }

    /**
     * 게시판 + 분류 + 검색어 검색
     * 인기 게시글 없음
     */
    public List<S> getPostListByCategoryIdAndTitle(BoardGroup boardGroup, Long categoryId, String keyword, Pageable pageable) {

        Page<E> list = commonPostRepository.findByDtypeAndCategoryIdAndTitleContainingIgnoreCase(boardGroup.getTable(), categoryId, keyword, pageable);
        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
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
