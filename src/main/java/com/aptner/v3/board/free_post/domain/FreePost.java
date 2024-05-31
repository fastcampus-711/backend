package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("FreePost")
public class FreePost extends CommonPost {
    private String blindBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime blindAt;

    public FreePost() {}

    public FreePost(Member member, Category category, String title, String content, boolean visible, LocalDateTime blindAt, String blindBy) {
        super(member, category, title, content, visible);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }

    public static FreePost of(Member member, Category category, String title, String content, boolean visible, LocalDateTime blindAt, String blindBy) {
        return new FreePost(member, category, title, content, visible, blindAt, blindBy);
    }

    public FreePostDto toDto(FreePost entity) {

        return FreePostDto.builder()
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
                .blindBy(entity.getBlindBy())
                .blindAt(entity.getBlindAt())
                .build();
    }
}
