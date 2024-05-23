package com.aptner.v3.reaction.dto;

import com.aptner.v3.reaction.domain.Reaction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static com.aptner.v3.CommunityApplication.modelMapper;

public class ReactionDto {
    @Getter
    public static class Request {
        @NotNull
        private long userId;
        @NotNull
        private ReactionTarget reactionTarget;
        @NotNull
        private long targetId;
        @NotNull
        private ReactionType reactionType;

        public Reaction toEntity() {
            return (Reaction) modelMapper().map(this, reactionTarget.getReactionClazz());
        }
    }
}