package com.aptner.v3.board.common.reaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ReactionDto {
    @Getter
    public static class Request {

        @Setter
        private long userId;
        @NotNull
        private long targetId;
        @NotNull
        private ReactionType reactionType;
    }
}