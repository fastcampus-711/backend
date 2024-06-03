package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.PostSpecification;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

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
    private final ReactionRepository<PostReaction> postReactionRepository;

    public CommonPostService(MemberRepository memberRepository,
                             CategoryRepository categoryRepository,
                             @Qualifier("commonPostRepository") CommonPostRepository<E> commonPostRepository,
                             ReactionRepository<PostReaction> postReactionRepository
    ) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.commonPostRepository = commonPostRepository;
        this.postReactionRepository = postReactionRepository;
    }

    public Page<T> getPostList(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, Pageable pageable) {

        Specification<E> spec = geteSpecification(boardGroup, categoryId, keyword, status, userId);
        Page<E> posts = commonPostRepository.findAll(spec, pageable);
        return posts.map(e -> (T) e.toDto());
    }

    public Page<T> getPostListWithComment(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, Pageable pageable) {

        Specification<E> spec = geteSpecification(boardGroup, categoryId, keyword, status, userId);
        Page<E> posts = commonPostRepository.findAll(spec, pageable);
        return posts.map(e -> (T) e.toDtoWithComment());
    }

    private static <E extends CommonPost> Specification<E> geteSpecification(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId) {
        Specification<E> spec = (Specification<E>) Specification
                .where(PostSpecification.hasBoardGroup(boardGroup))
                .and(PostSpecification.hasCategoryId(categoryId))
                .and(PostSpecification.hasKeyword(keyword))
                .and(PostSpecification.hasStatus(status))
                .and(PostSpecification.hasAuthor(userId));
        return spec;
    }

    public T getPost(BoardGroup boardGroup, long postId, Long userId) {
        // post
        E post = commonPostRepository.findByDtypeAndId(boardGroup.getTable(), postId)
                .orElseThrow(InvalidTableIdException::new);
        // 조회수
        post.plusHits();
        return (T) post.toDto();
    }

    public ReactionType getPostReactionType(Long userId, Long postId) {
        AtomicReference<ReactionType> reactionTypeRef = new AtomicReference<>(ReactionType.DEFAULT);

        postReactionRepository.findByUserIdAndTargetIdAndDtype(userId, postId, "PostReaction")
                .ifPresent(reaction -> reactionTypeRef.set(reaction.getReactionType()));

        return reactionTypeRef.get();
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
        verifyDeletePost(dto);
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

    protected E verifyDeletePost(T dto) {
        return verifyPost(dto);
    }

    protected E verifyPost(T dto) {
        // id check
        if (dto.getId() == null) {
            log.error("POST ID 없음");
            throw new PostException(INVALID_REQUEST);
        }

        // exists
        E post = commonPostRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        // 자신이 작성한 글이 아닌 경우
        if (!post.getMember().getId().equals(dto.getMemberDto().getId())) {
            log.error("POST 저장에 MEMBER ID 가 로그인 유저와 상이함");
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }

        // Board 속한 게시글 수정/삭제
        if (StringUtils.isNotEmpty(post.getDtype())
                && !post.getDtype().equals(dto.getBoardGroup())) {
            log.error("속한 카테고리가 아님: {} | {}", post.getDtype(), dto.getBoardGroup());
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

    private void logGenericTypes() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
            log.debug("Generic types: (E)Entity = {}, (T)DTO = {}, (Q)Request = {}, (S)Response = {}",
                    typeArguments[0].getTypeName(),
                    typeArguments[1].getTypeName(),
                    typeArguments[2].getTypeName(),
                    typeArguments[3].getTypeName());
        } else {
            log.debug("No generic type information available.");
        }
    }
}
