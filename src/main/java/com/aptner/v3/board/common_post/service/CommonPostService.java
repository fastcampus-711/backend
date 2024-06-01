package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.service.CountCommentsAndReactionApplyService;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.INVALID_REQUEST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;


@Slf4j
@Service
@Transactional
@Qualifier("commonPostService")
public class CommonPostService<E extends CommonPost,
        T extends CommonPostDto,
        Q extends CommonPostDto.CommonPostRequest,
        S extends CommonPostDto.CommonPostResponse> {

    private final CommonPostRepository<E> commonPostRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CountCommentsAndReactionApplyService<E> countOfReactionAndCommentApplyService;

    public CommonPostService(MemberRepository memberRepository, CategoryRepository categoryRepository, @Qualifier("commonPostRepository") CommonPostRepository<E> commonPostRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.countOfReactionAndCommentApplyService = new CountCommentsAndReactionApplyService<>(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

    /**
     * 게시판 + 분류 검색
     * 자유게시판 : 인기게시글
     */
    public Page<T> getPostListByCategoryId(BoardGroup boardGroup, Long categoryId, Status status, Pageable pageable) {

        Page<E> list = null;
        if (categoryId == 0) {
            if (status != null) {
                // 민원 게시판, 나눔 게시판, Qna 게시판
                list = findByDtypeAndStatus(boardGroup, status, pageable);
            } else {
                // 자유 게시판 조회
                if (BoardGroup.FREES.equals(boardGroup)) {
                    return Top3PostsWhenFirstPage(boardGroup, pageable);
                } else {
                    list = commonPostRepository.findByDtype(boardGroup.getTable(), pageable);
                }
            }
        } else {
            if (status != null) {
                list = findByDtypeAndCategoryIdAndStatus(boardGroup, categoryId, status, pageable);
            } else {
                // 게시판 + 카테고리 조회 ( 자유게시판 )
                list = commonPostRepository.findByDtypeAndCategoryId(boardGroup.getTable(), categoryId, pageable);
            }
        }

        return list.map(e -> (T) e.toDto());
    }

    private Page<T> Top3PostsWhenFirstPage(BoardGroup boardGroup, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 7일간 인기 게시글
        List<E> topPosts = commonPostRepository.findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfterAndDtype(sevenDaysAgo, boardGroup.getTable(), pageable);
        List<Long> topPostIds = topPosts.stream().map(CommonPost::getId).collect(Collectors.toList());
        if (pageable.getPageNumber() == 0) {

            // 7일간 인기 게시글 중복 제거
            Pageable adjustPageable = Pageable.ofSize(pageable.getPageSize() - topPosts.size()).withPage(pageable.getPageNumber());
            Page<E> posts = commonPostRepository.findAllExcludingTopPostsAndDtype(topPostIds, boardGroup.getTable(), adjustPageable);

            // 합치기
            List<T> topPostsDto = topPosts.stream().map(e -> {
                T dto = (T) e.toDto();
                dto.setHot(true);
                return dto;
            }).toList();
            List<T> postsDto = posts.map(e -> (T) e.toDto()).getContent();

            List<T> combined = new ArrayList<>(topPostsDto);
            combined.addAll(postsDto);
            return new PageImpl<T>(combined, pageable, posts.getTotalElements() + topPosts.size());
        } else {

            // 7일간 인기 게시글 중복 제거
            Page<E> list = commonPostRepository.findAllExcludingTopPostsAndDtype(topPostIds, boardGroup.getTable(), pageable);
            return list.map(e -> (T) e.toDto());
        }
    }

    public Page<E> findByDtypeAndStatus(BoardGroup boardGroup, Status status, Pageable pageable) {
        return commonPostRepository.findByDtype(boardGroup.getTable(), pageable);
    }

    public Page<E> findByDtypeAndCategoryIdAndStatus(BoardGroup boardGroup, Long categoryId, Status status, Pageable pageable) {
        return commonPostRepository.findByDtypeAndCategoryId(boardGroup.getTable(), categoryId, pageable);
    }

    /**
     * 게시판 + 분류 + 검색어 검색
     * 인기 게시글 없음
     */
    public Page<T> getPostListByCategoryIdAndTitle(BoardGroup boardGroup, Long categoryId, String keyword, Pageable pageable) {
        Page<E> list = commonPostRepository.findByDtypeAndCategoryIdAndTitleContainingIgnoreCase(boardGroup.getTable(), categoryId, keyword, pageable);
        return list.map(e -> (T) e.toDto());
    }

    public T getPost(long postId) {

        // Dtype 넣어서 조회 하도록 수정
        //Logic: 조회수 +1
        E post = commonPostRepository.findByComments_CommonPostId(postId)
                .orElse(
                        commonPostRepository.findById(postId)
                                .orElseThrow(InvalidTableIdException::new)
                );
        post.plusHits();

        //Logic: 댓글 수 갱신
        post.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(post.getComments())
        );

        return (T) post.toDto();
    }

    public T createPost(T dto) {

        Member member = verifyMember(dto);
        Category category = verifyCategory(dto);
        verifyCreatePost(dto);
        E entity = (E) dto.toEntity(member, category);
        E saved = commonPostRepository.save(entity);
        T postDto = (T) saved.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

    public T updatePost(T dto) {

        log.debug("updatePost : dto {}", dto);
        Member member = verifyMember(dto);
        Category category = verifyCategory(dto);
        E post = verifyPost(dto);

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getImageUrls() != null) {
            post.setImageUrls(dto.getImageUrls());
        }

        commonPostRepository.flush();
        log.debug("updatePost : {}", post);

        T postDto = (T) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

    public long deletePost(long postId, T dto) {
        verifyPost(dto);

        commonPostRepository.deleteById(postId);
        return postId;
    }

    private void verifyCreatePost(T dto) {
        int imageUploadCount = 5;
        if (dto.getImageUrls() != null &&
                dto.getImageUrls().size() > imageUploadCount) {
            log.error("createPost - image count exceed : {}", dto.getImageUrls().size());
            throw new PostException(INVALID_REQUEST);
        }
    }

    protected E verifyPost(T dto) {
        // id check
        if (dto.getId() == null) {
            log.error("POST ID 없음");
            throw new PostException(INVALID_REQUEST);
        }
        // exists
        E post;

        post = commonPostRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        // 자신이 작성한 글이 아닌 경우
        if (!post.getMember().getId().equals(dto.getMemberDto().getId())) {
            log.error("POST 저장에 MEMBER ID 가 로그인 유저와 상이함");
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }

        // Board 속한 게시글 수정/삭제
        if (StringUtils.isNotEmpty(post.getDtype())
                && !post.getDtype().equals(dto.getBoardGroup().getTable())) {
            log.error("속한 카테고리가 아님: {} | {}", post.getDtype(), dto.getBoardGroup().getTable());
            throw new PostException(INVALID_REQUEST);
        }
        return post;
    }

    protected Category verifyCategory(T dto) {
        // id check
        if (dto.getCategoryDto().getId() == null) {
            log.error("카테고리 ID 없음");
            throw new CategoryException(INVALID_REQUEST);
        }
        // exists
        try {
            return categoryRepository.getReferenceById(dto.getCategoryDto().getId());
        } catch (EntityNotFoundException e1) {
            log.error("카테고리 ID DB에 없음");
            throw new CategoryException(_NOT_FOUND);
        }
    }

    protected Member verifyMember(T dto) {
        if (dto.getMemberDto().getId() == null) {
            log.error("MEMBER ID 없음");
            throw new UserException(INVALID_REQUEST);
        }

        try {
            return memberRepository.getReferenceById(dto.getMemberDto().getId());
        } catch (EntityNotFoundException e) {
            log.error("MEMBER ID DBd에 없음");
            throw new UserException(_NOT_FOUND);
        }
    }
}
