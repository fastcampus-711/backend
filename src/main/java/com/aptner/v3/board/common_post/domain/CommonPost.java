package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@ToString(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class CommonPost extends BaseTimeEntity
        implements ReactionAndCommentCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Member member;

    @Column(nullable = true)
    private Long memberId;

    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Category category;

    @Setter
    @Column(length = 200)
    private String title;

    @Setter
    @Column(length = 500)
    private String content;

    @Setter
    @ElementCollection
    @Column(name = "post_images")
    List<String> imageUrls;

    @Setter
    private long hits;

    @Setter
    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    @Setter
    private long countOfComments;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ColumnDefault(value = "true")
    private boolean visible;

    private boolean deleted;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public CommonPost(Member member, Category category, String title, String content, boolean visible) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.visible = visible;
    }

    public static CommonPost of(Member member, Category category, String title, String content, boolean visible) {
        return new CommonPost(member, category, title, content, visible);
    }

    public CommonPostDto toDto() {
        CommonPost entity = this;
        CommonPostDto build = CommonPostDto.builder()
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

        return build;
    }

    public CommonPost updateCountOfComments(long countOfComments) {
        this.countOfComments = countOfComments;
        return this;
    }

    public CommonPost plusHits() {
        this.hits++;

        return this;
    }
}
