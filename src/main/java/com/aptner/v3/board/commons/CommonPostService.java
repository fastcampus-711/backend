package com.aptner.v3.board.commons;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.domain.SortType;
import com.aptner.v3.category.CategoryCode;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import com.aptner.v3.reaction.service.CountOfReactionAndCommentApplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Primary
@Service
@Transactional
public class CommonPostService<
        E extends CommonPost,
        Q extends CommonPostDto.CommonRequest,
        S extends CommonPostDto.CommonResponse,
        T extends CommonPostDto> {
    private final MemberRepository memberRepository;
    private final CommonPostRepository<E> commonPostRepository;
    private final CountOfReactionAndCommentApplyService<E> countOfReactionAndCommentApplyService;

    public CommonPostService(CommonPostRepository<E> commonPostRepository,
                             MemberRepository memberRepository) {
        this.countOfReactionAndCommentApplyService = new CountOfReactionAndCommentApplyService<>(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
        this.memberRepository = memberRepository;
    }


    public S getPost(long postId) {
        //Logic: 조회수 +1
        E commonPost = commonPostRepository.findByComments_CommonPostId(postId)
                .orElse(
                        commonPostRepository.findById(postId)
                                .orElseThrow(InvalidTableIdException::new) //@todo Exception
                );
        commonPost.plusHits();

        //Logic: 댓글 수 갱신
        commonPost.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(commonPost.getComments())
        );

        return (S) commonPost.toResponseDtoWithComments();
    }


    private LocalDateTime getSevenDayAgo(){
        return LocalDateTime.now().minus(7, ChronoUnit.DAYS);
    }

    @Transactional
    public List<E> updateTopPosts() {
        List<E> topPosts = commonPostRepository.findTop3ByOrderByHitsDescAndCreatedAtAfter(getSevenDayAgo(), PageRequest.of(0, 3));
                //.findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfter(PageRequest.of(0, 3));
        return topPosts;
    }

    public List<S> getPostList(HttpServletRequest request, Integer page) {
        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost")) {
            list = commonPostRepository.findAll();
        } else {
            list = commonPostRepository.findByDtype(dtype);
        }

        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
    }

    public List<S> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        String dtype = getDtype(request);

        List<E> list;
        if (dtype.equals("CommonPost"))
            list = commonPostRepository.findByTitleContaining(keyword, pageable).getContent();
        else
            list = commonPostRepository.findByTitleContainingAndDtype(keyword, dtype, pageable).getContent();

        return list.stream()
                .map(e -> (S) e.toResponseDtoWithoutComments())
                .toList();
    }

    public S createPost(Q request) {

        Member member = memberRepository.getReferenceById(request.getUserId());
        T dto = (T) request.toDto(member);
        E entity = (E) dto.toEntity();
        E saved = commonPostRepository.save(entity);
        log.debug("saved : {}", saved);
        return (S) saved.toResponseDtoWithoutComments();
    }

    public S updatePost(long postId, Q requestDto) {
        E e = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        // @todo Exception & verify
        // @일단 머리아퍼 넣어둬...
        return (S) e.updateByUpdateRequest(requestDto)
                .toResponseDtoWithoutComments();
    }

    public long deletePost(long id) {
        commonPostRepository.deleteById(id);
        return id;
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
}
