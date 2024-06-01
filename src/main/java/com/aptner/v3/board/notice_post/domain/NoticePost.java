package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("NoticePost")
public class NoticePost extends CommonPost {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postAt;

    public NoticePost() {
    }

    public NoticePost(Member member, Category category, String title, String content, boolean visible, List<String> imageUrls, LocalDateTime postAt) {
        super(member, category, title, content, visible, imageUrls);
        this.postAt = postAt;
    }

    public static NoticePost of(Member member, Category category, String title, String content, boolean visible, List<String> imageUrls, LocalDateTime postAt) {
        return new NoticePost(member, category, title, content, visible, imageUrls, postAt);
    }

    @Override
    public NoticePostDto toDto() {

        NoticePost entity = this;
        return NoticePostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(MemberUtil.getMemberId() != entity.getMember().getId())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}