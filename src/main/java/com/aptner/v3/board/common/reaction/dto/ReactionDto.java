package com.aptner.v3.board.common.reaction.dto;

import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ReactionDto {
    @Getter
    public static class ReactionRequest {

        @Setter
        private long userId;
        @NotNull
        private long targetId;
        @NotNull
        private ReactionType reactionType;
    }

    @Getter
    @AllArgsConstructor
    public static class ReactionResponse {

        @Setter
        private long userId;
        @NotNull
        private long targetId;
        @NotNull
        private ReactionType reactionType;

        private ReactionColumnsDto reactionColums;
    }
}