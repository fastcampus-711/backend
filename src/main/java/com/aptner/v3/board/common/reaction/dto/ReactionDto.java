package com.aptner.v3.board.common.reaction.dto;

import com.aptner.v3.board.common.reaction.domain.Reaction;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.modelmapper.ModelMapper;

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
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();
            return (Reaction) modelMapper.map(this, reactionTarget.getReactionClazz());
        }
    }
}