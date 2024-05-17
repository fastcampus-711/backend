package com.aptner.v3.board.comment.domain;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted = true where id = ?")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private boolean visible = true;
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_post_id")
    private CommonPost commonPost;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER)
    private List<Comment> childComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    public Comment() {
    }

    public static Comment of(CommonPost commonPost, CommentDto.Request request) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        request.setCommonPost(commonPost);

        return modelMapper
                .map(request, Comment.class);
    }

    public static Comment of(Comment comment, CommentDto.Request request) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        request.setParentComment(comment);
        return modelMapper.map(request, Comment.class);
    }

    public Comment update(CommentDto.Request requestDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        modelMapper.map(requestDto, this);
        return this;
    }

    public CommentDto.Response toResponseDto() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

//        modelMapper
//                .createTypeMap(this, CommentDto.Response.class)
//                .addMappings(mapper -> mapper.map(comment -> comment.getParentComment().getId(), CommentDto.Response::setParentCommentId));
        return modelMapper.map(this, CommentDto.Response.class);
    }
}
