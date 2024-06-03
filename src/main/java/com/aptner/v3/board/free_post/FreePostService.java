package com.aptner.v3.board.free_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.PostSpecification;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Qualifier("freePostService")
public class FreePostService extends CommonPostService<FreePost, FreePostDto, FreePostDto.FreePostRequest, FreePostDto.FreePostResponse> {

    private final CommonPostRepository<FreePost> commonPostRepository;

    public FreePostService(MemberRepository memberRepository,
                           CategoryRepository categoryRepository,
                           CommonPostRepository<FreePost> commonPostRepository,
                           ReactionRepository<PostReaction> postReactionRepository
    ) {
        super(memberRepository, categoryRepository, commonPostRepository, postReactionRepository);
        this.commonPostRepository = commonPostRepository;
    }

    @Override
    public Page<FreePostDto> getPostList(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, Pageable pageable) {
        if (pageable.getPageNumber() == 0) {
            // (HOT) 인기 검색 예외 처리
            return getFirstPageWithTopPosts(boardGroup, categoryId, keyword, status, userId, pageable);
        }
        return super.getPostList(boardGroup, categoryId, keyword, status, userId, pageable);
    }

    public Page<FreePostDto> getPostListWithoutHotPost(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, Pageable pageable) {
        return super.getPostList(boardGroup, categoryId, keyword, status, userId, pageable);
    }

    private Page<FreePostDto> getFirstPageWithTopPosts(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, Pageable pageable) {

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        Specification<FreePost> spec = Specification
                .where(PostSpecification.<FreePost>hasBoardGroup(boardGroup))
                .and(PostSpecification.hasCategoryId(categoryId))
                .and(PostSpecification.hasKeyword(keyword))
                .and(PostSpecification.hasStatus(status))
                .and(PostSpecification.hasAuthor(userId));

        // 상위 3개 ORDER BY (p.hits + p.reactionColumns.countReactionTypeGood) DESC
        List<FreePost> topPosts = commonPostRepository.findTopPosts(sevenDaysAgo, boardGroup.getTable(), PageRequest.of(0, 3));
        List<Long> topPostIds = topPosts.stream().map(CommonPost::getId).collect(Collectors.toList());

        Page<FreePost> remainingPosts = commonPostRepository.findAll(Specification.where(spec)
                .and((root, query, criteriaBuilder) -> root.get("id").in(topPostIds).not()), PageRequest.of(0, pageable.getPageSize() - topPosts.size(), pageable.getSort()));

        List<FreePostDto> combinedPosts = Stream.concat(
                topPosts.stream().map(this::toFreePostDto),
                remainingPosts.getContent().stream().map(FreePost::toDto)
        ).collect(Collectors.toList());

        return new PageImpl<>(combinedPosts, pageable, remainingPosts.getTotalElements() + topPosts.size());
    }

    private FreePostDto toFreePostDto(FreePost entity) {
        FreePostDto dto = entity.toDto();
        dto.setHot(true);
        return dto;
    }
}
