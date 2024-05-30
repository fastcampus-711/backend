package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {

    @Getter
    @Setter
    public static class Request {
        private Member member;
        private boolean writer;
        private long postUserId;
        @NotBlank
        private String content;
        private boolean visible;
    }

    @Getter
    @Setter
    public static class Response {
        private long id;
        private String profileImageUuid;
        private String nickname;
        private LocalDateTime createdAt;
        private String content;
        private ReactionColumns reactionColumns;
        private boolean visible;
        private boolean writer;
        private boolean admin;
        private long commonPostId;
        private List<CommentDto.Response> childComments;
    }
}
