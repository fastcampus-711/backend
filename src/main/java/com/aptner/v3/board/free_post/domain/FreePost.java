package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("FreePost")
public class FreePost extends CommonPost {
    private String blindBy;
    private LocalDateTime blindAt;

    public FreePost() {}

    public FreePost(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, String blindBy, LocalDateTime blindAt) {
        super(member, category, title, content, imageUrls, visible);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }

    public static FreePost of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, LocalDateTime blindAt, String blindBy) {
        return new FreePost(member, category, title, content, imageUrls, visible, blindBy, blindAt);
    }
    @Override
    public FreePostDto toDto() {

        FreePost entity = this;
        Long currentUserId = MemberUtil.getMember().getId();

        ReactionType userReaction = entity.getReactions().stream()
                .filter(reaction -> reaction.getUserId().equals(currentUserId))
                .map(PostReaction::getReactionType)
                .findFirst()
                .orElse(null);

        return FreePostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .reactionType(userReaction)
                .countOfComments(entity.getCountOfComments())
                .visible(entity.isVisible())
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .blindBy(entity.getBlindBy())
                .blindAt(entity.getBlindAt())
                .build();
    }
}
