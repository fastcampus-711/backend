package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@DiscriminatorValue("QnaPost")
public class Qna extends CommonPost {
    private String type;

    @Setter
    @Enumerated(EnumType.STRING)
    private QnaStatus status;

    public Qna() {
    }

    public Qna(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, String type, QnaStatus status) {
        super(member, category, title, content, imageUrls, visible);
        this.type = type;
        this.status = status;
    }

    public static Qna of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible, String type, QnaStatus status) {
        return new Qna(member, category, title, content, imageUrls, visible, type, status);
    }

    @Override
    public QnaDto toDto() {
        Qna entity = this;
        return QnaDto.builder()
                .id(entity.getId())
                // member
                .memberDto(MemberDto.from(entity.getMember()))
                // post
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(entity.isVisible())
                 // qna
                .type(entity.getType())
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

    @Override
    public CommonPostDto toDtoWithComment() {
        Qna entity = this;
        QnaDto build = QnaDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .commentDto(Optional.ofNullable(entity.getComments())
                        .orElse(Collections.emptySet())
                        .stream().map(Comment::toDto)
                        .collect(Collectors.toSet())
                )
                .visible(entity.isVisible())
                // qna
                .type(entity.getType())
                .status(entity.getStatus())
                //
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
    }
}
