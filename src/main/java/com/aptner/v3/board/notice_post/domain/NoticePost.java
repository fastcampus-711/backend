package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("NoticePost")
public class NoticePost extends CommonPost {
    /**
     * 중요글 
     **/
    @ColumnDefault(value = "false")
    private boolean isImport;
    /**
     * 의무 공개
     **/
    @ColumnDefault(value = "false")
    private boolean isDuty;
    /**
     * 일정 등록
     **/
    private LocalDateTime scheduleStartAt;
    private LocalDateTime scheduleEndAt;
    /**
     * 등록 게시
     **/
    private LocalDateTime postAt;

    public NoticePost() {
    }

    public NoticePost(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, boolean isImport, boolean isDuty, LocalDateTime scheduleStartAt, LocalDateTime scheduleEndAt, LocalDateTime postAt) {
        super(member, category, title, content, imageUrls, visible);
        this.isImport = isImport;
        this.isDuty = isDuty;
        this.scheduleStartAt = scheduleStartAt;
        this.scheduleEndAt = scheduleEndAt;
        this.postAt = postAt;
    }

    public static NoticePost of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, LocalDateTime postAt) {
        return new NoticePost(member, category, title, content, imageUrls, visible, false, false, null, null, postAt);
    }

    public static NoticePost of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, boolean isImport, boolean isDuty, LocalDateTime scheduleStartAt, LocalDateTime scheduleEndAt, LocalDateTime postAt) {
        return new NoticePost(member, category, title, content, imageUrls, visible, isImport, isDuty, scheduleStartAt, scheduleEndAt, postAt);
    }

    @Override
    public NoticePostDto toDto() {

        NoticePost entity = this;

        Long currentUserId = MemberUtil.getMember().getId();

        ReactionType userReaction = entity.getReactions().stream()
                .filter(reaction -> reaction.getUserId().equals(currentUserId))
                .map(PostReaction::getReactionType)
                .findFirst()
                .orElse(null);

        return NoticePostDto.builder()
                .id(entity.getId())
                // user
                .memberDto(MemberDto.from(entity.getMember()))
                // post
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .visible(entity.isVisible())
                // post info
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .reactionType(userReaction)
                .countOfComments(entity.getCountOfComments())
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