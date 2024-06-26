package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class CommonPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Member member;

    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Category category;

    @Setter
    @Column(length = 200)
    private String title;

    @Setter
    @Column(length = 2500)
    private String content;

    @Setter
    @ElementCollection
    @Column(name = "post_images")
    List<String> imageUrls;

    @ColumnDefault(value = "true")
    private boolean visible;

    @Setter
    private long hits;

    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    @Setter
    private long countOfComments;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @ToString.Exclude
    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostReaction> reactions = new HashSet<>();

    private boolean deleted;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public CommonPost(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.visible = visible;
    }

    public static CommonPost of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible) {
        return new CommonPost(member, category, title, content, imageUrls, visible);
    }

    public void setReactionColumns(long countReactionTypeGood, long countReactionTypeBad) {
        if (this.reactionColumns == null) {
            this.reactionColumns = new ReactionColumns();
        }
        this.reactionColumns.setCountReactionTypeGood(countReactionTypeGood);
        this.reactionColumns.setCountReactionTypeBad(countReactionTypeBad);
    }

    public void plusHits() {
        this.hits++;
    }

    /* 댓글을 포함 하지 않는 경우 */
    public CommonPostDto toDto() {
        CommonPost entity = this;
        Long currentUserId = MemberUtil.getMember().getId();

        // reaction
        ReactionType userReaction = getReactionType(entity, currentUserId);

        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                // member
                .memberDto(MemberDto.from(entity.getMember()))
                // post
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .visible(entity.isVisible())
                // reaction
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .reactionType(userReaction)
                // comment
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

        return build;
    }

    /* 댓글을 포함 하는 경우 */
    public CommonPostDto toDtoWithComment() {
        CommonPost entity = this;
        Long currentUserId = MemberUtil.getMember().getId();

        // reaction
        ReactionType userReaction = getReactionType(entity, currentUserId);

        // comment
        Set<CommentDto> commentDtos = Optional.ofNullable(entity.getComments())
                .orElse(Collections.emptySet())
                .stream()
                .map(Comment::toDto)
                .collect(Collectors.toSet());

        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                // member
                .memberDto(MemberDto.from(entity.getMember()))
                // post
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .visible(entity.isVisible())
                // reaction
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .reactionType(userReaction)
                // comment
                .countOfComments(entity.getCountOfComments())
                 /* 댓글 생성 */
                .commentDto(commentDtos)
                // category
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                // base
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
    }

    public static ReactionType getReactionType(CommonPost entity, Long currentUserId) {
        ReactionType userReaction = entity.getReactions().stream()
                .filter(reaction -> reaction.getUserId().equals(currentUserId))
                .map(PostReaction::getReactionType)
                .findFirst()
                .orElse(null);
        return userReaction;
    }
}
