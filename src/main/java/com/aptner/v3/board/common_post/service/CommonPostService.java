package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.common.reaction.service.CountOfReactionAndCommentApplyService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Primary
@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class CommonPostService<
        E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response>
        extends CountOfReactionAndCommentApplyService<CommonPost> {
    private final CommonPostRepository<E> commonPostRepository;

    public CommonPostService(CommonPostRepository<E> commonPostRepository) {
        super((JpaRepository<CommonPost, Long>) commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

    public S getPost(long postId) {
        E commonPost = commonPostRepository.findByComments_CommonPostId(postId)
                .orElse(
                        commonPostRepository.findById(postId)
                                .orElseThrow(InvalidTableIdException::new)
                );

        commonPost.updateCountOfComments(countComments(commonPost.getComments()));

        return (S) commonPost.toResponseDtoWithComments();

    }
    //7일 전 날짜를 commonPostRepository 에 반환하는 메소드
    public LocalDateTime getSevenDayAgo(){
        return LocalDateTime.now().minus(7, ChronoUnit.DAYS);
    }
    @Scheduled(cron = "0 0 0 ? * MON") // 0초/ 0분/ 0시/ 아무날짜/ 모든월/ 월요일/ 년도생략시 현재 년도 적용
    @Transactional
    public List<E> updateTopPosts() {
        List<E> topPosts = commonPostRepository.findTop3ByOrderByHitsDescAndCreatedAtAfter(getSevenDayAgo(), PageRequest.of(0, 3));
                //.findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfter(PageRequest.of(0, 3));
        return topPosts;
    }

    public List<S> getPostList(HttpServletRequest request, Integer page) {
        String dtype = getDtype(request);

        List<E> list;
        List<E> topPostsList;
        if (dtype.equals("CommonPost")) {
            list = commonPostRepository.findAll();
        }else if (dtype.equals("FreePost")) {
            //자유게시판의 1페이지일 경우 7일 이내의 조회수+공감수가 가장 높은 3개의 글을 조회
            if (page == 1) {
                topPostsList = updateTopPosts();
                //인기글3개와 나머지 글 7개를 합쳐서 반환해야함
                /*topPostsList.addAll(commonPostRepository.findByDtype(dtype));
                return topPostsList.stream()
                        .map(e -> (S) e.toResponseDtoWithoutComments())
                        .toList();*/
                List<E> mergedList = Stream.concat(topPostsList.stream(), commonPostRepository.findByDtype(dtype).stream()).toList();
                return mergedList.stream()
                        .map(e -> (S) e.toResponseDtoWithoutComments())
                        .toList();
            } else {
                list = commonPostRepository.findByDtype(dtype);
            }
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

    public S createPost(Q requestDto) {
        E entity = (E) requestDto.toEntity();
        commonPostRepository.save(entity);
        return (S) entity.toResponseDtoWithoutComments();
    }

    public S updatePost(long postId, Q requestDto) {
        return (S) commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new)
                .updateByUpdateRequest(requestDto)
                .toResponseDtoWithoutComments();
    }

    public long deletePost(long id) {
        commonPostRepository.deleteById(id);
        return id;
    }

    private String getDtype(HttpServletRequest request) {
        String[] URIs = request.getRequestURI()
                .split("/");
        String target = URIs.length <= 2 ? "" : URIs[2];

        return Arrays.stream(CategoryCode.values())
                .filter(c -> c.getURI().equals(target))
                .findFirst()
                .orElseGet(() -> CategoryCode.공통)
                .getDtype();
    }

    public List<S> getPostListByCategoryId(Long categoryId, HttpServletRequest request) {
        String dtype = getDtype(request);
        List<E> list;
        if (categoryId == null) {
            list = commonPostRepository.findByDtype(dtype);

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

    /*public List<Category> getCategoryList(Long postId, HttpServletRequest request) {
        String dtype = getDtype(request);
        Menu menu = (Menu) menuRepository.findByName(dtype);
        List<Category> categoryList = categoryRepository.findByMenuId(menu.getId());
        return categoryList;
    }*/


}
