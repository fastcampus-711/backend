package com.aptner.v3.board.comment.domain;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.domain.CreatedInfo;
import jakarta.persistence.*;
import lombok.Getter;

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

    public Comment(long postId, String content) {

    }

    public static Comment of(long postId, CommentDto.AddRequest requestDto) {
        return null;
    }
}
