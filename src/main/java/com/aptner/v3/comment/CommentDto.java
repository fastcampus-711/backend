package com.aptner.v3.comment;

import com.aptner.v3.reaction.domain.ReactionColumns;
import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CommentDto {

    @Getter
    @Setter
    public static class Request {
        private long userId;
        private long postUserId;
        @NotBlank
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
        private ReactionColumns reactionColumns;
        private boolean visible;
        private boolean admin;
        private long commonPostId;
        private List<CommentDto.Response> childComments;
    }
}
