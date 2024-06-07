package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("ComplaintPost")
public class Complain extends CommonPost {

    @Setter
    @Enumerated(EnumType.STRING)
    private ComplainStatus status;

    public Complain() {
    }

    public Complain(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, ComplainStatus status) {
        super(member, category, title, content, imageUrls, visible);
        this.status = status;
    }

    public static Complain of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, ComplainStatus status) {
        return new Complain(member, category, title, content, imageUrls, visible, status);
    }
    @Override
    public ComplainDto toDto() {
        Complain entity = this;

        Long currentUserId = MemberUtil.getMember().getId();

        ReactionType userReaction = entity.getReactions().stream()
                .filter(reaction -> reaction.getUserId().equals(currentUserId))
                .map(PostReaction::getReactionType)
                .findFirst()
                .orElse(null);

        return ComplainDto.builder()
                .id(entity.getId())
                // member
                .memberDto(MemberDto.from(entity.getMember()))
                // post
                .title(entity.getTitle())                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .reactionType(userReaction)
                .countOfComments(entity.getCountOfComments())
                .visible(entity.isVisible())
                // complaint
                .status(entity.getStatus())
                // category
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                // base
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
