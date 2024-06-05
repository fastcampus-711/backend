package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ListIndexBase;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString
@SQLDelete(sql = "UPDATE comment SET deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class Comment extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ListIndexBase(1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommonPost commonPost;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Setter
    @Column(nullable = false, length = 500)
    private String content;

    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    @Setter
    @Column(updatable = false)
    private Long parentCommentId;

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<Comment> childComments;

    @Setter
    @ColumnDefault(value = "true")
    private boolean visible;

    @Setter
    @ColumnDefault(value = "false")
    private boolean isTop;

    private boolean deleted;
    private int countOfReports;
    protected Comment() {}

    public Comment(CommonPost commonPost, Member member, String content, Long parentCommentId, boolean visible, boolean isTop) {
        this.commonPost = commonPost;
        this.member = member;
        this.content = content;
        this.visible = visible;
        this.isTop = isTop;
    }

    public static Comment of(CommonPost commonPost, Member member, String content, boolean visible, boolean isTop) {
        return new Comment(commonPost, member, content, null, visible, isTop);
    }

    public void addChildComment(Comment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    public void setReactionColumns(long countReactionTypeGood, long countReactionTypeBad) {
        if (this.reactionColumns == null) {
            this.reactionColumns = new ReactionColumns();
        }
        this.reactionColumns.setCountReactionTypeGood(countReactionTypeGood);
        this.reactionColumns.setCountReactionTypeBad(countReactionTypeBad);
    }
    public int plusCommentReportCount(){
        return countOfReports++;
    }
    public CommentDto toDto() {
        Comment entity = this;

        Set<CommentDto> commentDtos = Optional.ofNullable(entity.getChildComments())
                .orElse(Collections.emptySet())
                .stream()
                .map(Comment::toDto)
                .collect(Collectors.toSet());

        CommentDto build = CommentDto.builder()
                .memberDto(MemberDto.from(entity.getMember()))
                .postId(entity.getCommonPost().getId()) // @todo
                // comment
                .commentId(entity.getId())
                .parentCommentId(entity.getParentCommentId())
                .childComments(commentDtos)
                .content(entity.getContent())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .visible(entity.isVisible())
                .isTop(entity.isTop())
                // base
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
    }
}
