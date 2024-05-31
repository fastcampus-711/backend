package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common.reaction.service.CountCommentsAndReactionApplyService;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.INVALID_REQUEST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;


@Slf4j
@Primary
@Service
@Transactional
public class CommonPostService<E extends CommonPost,
        T extends CommonPostDto,
        Q extends CommonPostDto.CommonPostRequest,
        S extends CommonPostDto.CommonPostResponse> {

    private final CommonPostRepository<E> commonPostRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CountCommentsAndReactionApplyService<E> countOfReactionAndCommentApplyService;
    private final ReactionRepository<PostReaction> postReactionRepository;
    private final ReactionRepository<CommentReaction> commentReactionRepository;

    public CommonPostService(MemberRepository memberRepository,
                             CategoryRepository categoryRepository,
                             CommonPostRepository<E> commonPostRepository,
                             ReactionRepository<PostReaction> postReactionRepository,
                             ReactionRepository<CommentReaction> commentReactionRepository) {

        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.countOfReactionAndCommentApplyService = new CountCommentsAndReactionApplyService<>(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
        this.postReactionRepository = postReactionRepository;
        this.commentReactionRepository = commentReactionRepository;
    }

    /**
     * 게시판 + 분류 검색
     * 자유게시판 : 인기게시글
     */
    public Page<T> getPostListByCategoryId(BoardGroup boardGroup, Long categoryId, Pageable pageable) {

        Page<E> list;
        if (categoryId == 0) {
            // 게시판 조회
            if (BoardGroup.FREES.equals(boardGroup)) {
                if (pageable.getPageNumber() == 1) {
                    // 인기 게시글
                }
            }

            list = commonPostRepository.findByDtype(boardGroup.getTable(), pageable);
        } else {
            // 게시판 + 카테고리 조회
            list = commonPostRepository.findByDtypeAndCategoryId(boardGroup.getTable(), categoryId, pageable);
        }

        return list.map(e -> (T) T.from(e));
    }

    /**
     * 게시판 + 분류 + 검색어 검색
     * 인기 게시글 없음
     */
    public Page<T> getPostListByCategoryIdAndTitle(BoardGroup boardGroup, Long categoryId, String keyword, Pageable pageable) {
        Page<E> list = commonPostRepository.findByDtypeAndCategoryIdAndTitleContainingIgnoreCase(boardGroup.getTable(), categoryId, keyword, pageable);
        return list.map(e -> (T) T.from(e));
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
        S commonPostDtoResponse = (S) commonPost.toResponseDtoWithComments();
        postReactionRepository.findByUserIdAndTargetIdAndDtype(MemberUtil.getMemberId(), postId, "PostReaction")
                .ifPresent(rst -> commonPostDtoResponse.setReactionType(rst.getReactionType()));

        Map<Long, ReactionType> mapCommentIdAndReactionType =commentReactionRepository.findByUserIdAndDtype(MemberUtil.getMemberId(), "CommentReaction")
                .stream()
                .map(commentReaction -> Map.entry(commentReaction.getTargetId(), commentReaction.getReactionType()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        commonPostDtoResponse.applyReactionTypeToComments(mapCommentIdAndReactionType);

        return commonPostDtoResponse;
    }

    public T createPost(T dto) {

        Member member = verifyMember(dto);
        Category category = verifyCategory(dto);
        E entity = (E) dto.toEntity(member, category);
        E saved = commonPostRepository.save(entity);
        return (T) T.from(saved);
    }

    public T updatePost(T dto) {

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
        return (T) T.from(post);
    }

    public long deletePost(long postId, T dto) {
        verifyPost(dto);

        commonPostRepository.deleteById(postId);
        return postId;
    }

    private E verifyPost(T dto) {
        // id check
        if (dto.getId() == null) {
            throw new PostException(INVALID_REQUEST);
        }
        // exists
        E post;
        try {
            post = commonPostRepository.getReferenceById(dto.getId());
        } catch (EntityNotFoundException e1) {
            throw new PostException(_NOT_FOUND);
        }

        // 자신이 작성한 글이 아닌 경우
        if (!post.getMember().getId().equals(dto.getMemberDto().getId())) {
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }

        // Board 속한 게시글 수정/삭제
        if (StringUtils.isNotEmpty(post.getDtype())
                && post.getDtype().equals(dto.getBoardGroup())) {
            throw new PostException(INVALID_REQUEST);
        }
        return post;
    }

    private Category verifyCategory(T dto) {
        // id check
        if (dto.getCategoryDto().getId() == null) {
            throw new CategoryException(INVALID_REQUEST);
        }
        // exists
        try {
            return categoryRepository.getReferenceById(dto.getCategoryDto().getId());
        } catch (EntityNotFoundException e1) {
            throw new CategoryException(_NOT_FOUND);
        }
    }

    private Member verifyMember(T dto) {
        if (dto.getMemberDto().getId() == null) {
            throw new UserException(INVALID_REQUEST);
        }

        try {
            return memberRepository.getReferenceById(dto.getMemberDto().getId());
        } catch (EntityNotFoundException e) {
            throw new UserException(_NOT_FOUND);
        }
    }
}
