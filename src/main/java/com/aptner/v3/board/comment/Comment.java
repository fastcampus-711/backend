package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.modelmapper.ModelMapper;

import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted = true where id = ?")
@NoArgsConstructor
public class Comment extends BaseTimeEntity implements ReactionAndCommentCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_post_id")
    private CommonPost commonPost;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER)
    private List<Comment> childComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    @ColumnDefault(value = "true")
    private boolean visible;

    @ColumnDefault(value = "false")
    private boolean admin;

    @ColumnDefault(value = "false")
    private boolean writer;

    @ColumnDefault(value = "false")
    private Boolean deleted;

    public static Comment of(CommonPost commonPost, CommentDto.Request request) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        return modelMapper
                .map(request, Comment.class).setCommonPost(commonPost);
    }

    public static Comment of(Comment comment, CommentDto.Request request) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        return modelMapper.map(request, Comment.class).setParentComment(comment);
    }

    public Comment updateByRequestDto(CommentDto.Request requestDto) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        modelMapper.map(requestDto, this);
        return this;
    }

    public CommentDto.Response toResponseDto() {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        return modelMapper.map(this, CommentDto.Response.class);
    }

    private Comment setCommonPost(CommonPost commonPost) {
        this.commonPost = commonPost;
        return this;
    }

    private Comment setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        return this;
    }
}
