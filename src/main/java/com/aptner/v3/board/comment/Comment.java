package com.aptner.v3.board.comment;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.global.util.ModelMapperUtil;
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
public class Comment extends ReactionColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_post_id")
    private CommonPost commonPost;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER)
    private List<Comment> childComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    @ColumnDefault(value = "true")
    private Boolean visible;

    @ColumnDefault(value = "false")
    private Boolean deleted;

    public static Comment of(CommonPost commonPost, CommentDto.Request request) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        request.setCommonPost(commonPost);

        return modelMapper
                .map(request, Comment.class);
    }

    public static Comment of(Comment comment, CommentDto.Request request) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        request.setParentComment(comment);
        return modelMapper.map(request, Comment.class);
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
}
