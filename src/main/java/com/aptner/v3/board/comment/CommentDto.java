package com.aptner.v3.board.comment;

import com.aptner.v3.board.common_post.domain.CommonPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CommentDto {

    @Getter
    @Setter
    public static class Request {
        private String content;
        private boolean visible;
        private CommonPost commonPost;
        private Comment parentComment;

    }

    @Getter
    @Setter
    public static class Response {
        private long id;
        private String content;
        private long countReactionTypeGood;
        private long countReactionTypeBad;
        private boolean visible;
        private long commonPostId;
        private List<CommentDto.Response> childComments;
    }
}
