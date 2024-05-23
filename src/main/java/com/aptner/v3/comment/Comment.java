package com.aptner.v3.comment;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.member.Member;
import com.aptner.v3.reaction.domain.Reactions;
import com.aptner.v3.reaction.service.ReactionAndCommentCalculator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

import static com.aptner.v3.CommunityApplication.modelMapper;

@Entity
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted = 1 where id = ?")
public class Comment implements ReactionAndCommentCalculator {

    /* ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 작성자 */
    @ManyToOne(optional = false)
    private Member member;

    /* 내용 */
    private String content;

    /* 공감 */
    @Embedded
    private Reactions reactions;

    /* 해당 게시글 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_post_id")
    private CommonPost commonPost;

    /* 자식 댓글 */
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER)
    private List<Comment> childComments;

    /* 부모 댓글 */
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    @ColumnDefault(value = "true")
    private boolean visible = true;

//    @ColumnDefault(value = "false")
//    private boolean admin = false;

    @ColumnDefault(value = "false")
    private Boolean deleted = false;

    public Comment updateByRequestDto(CommentDto.CommentRequest commentRequestDto) {
        modelMapper().map(commentRequestDto, this);
        return this;
    }

    public CommentDto.CommentResponse toResponseDto() {
        return modelMapper().map(this, CommentDto.CommentResponse.class);
    }

    protected Comment() {
    }

    private Comment(CommonPost commonPost, Member meber, Comment parentComment, String content, boolean visible) {
        this.commonPost = commonPost;
        this.member = meber;
        this.parentComment = parentComment;
        this.content = content;
        this.visible = visible;
    }

    public static Comment of(CommonPost post, Member member, String content, boolean visible) {
        return new Comment(post, member, null, content, visible);
    }

    public void addChildComment(Comment child) {
        child.setParentComment(this);
        this.getChildComments().add(child);
    }
}
