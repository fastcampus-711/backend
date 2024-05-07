package com.aptner.v3.board.comment.domain;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.domain.CreatedInfo;
import jakarta.persistence.*;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;

@Entity
@Getter
public class Comment extends CreatedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private boolean visible = true;
    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "common_post_id")
    private CommonPost commonPost;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    public Comment() {}

    public Comment(CommonPost commonPost, String content) {
        this.commonPost = commonPost;
        this.content = content;
    }

    public static Comment of(CommonPost commonPost, String content) {
        return new Comment(commonPost, content);
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
}
