package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.dto.CountOfReactionTypeDto;
import com.aptner.v3.global.util.ModelMapperUtil;
import org.modelmapper.ModelMapper;

public interface ReactionAndCommentCalculator {
    default ReactionAndCommentCalculator updateCountOfReactions(CountOfReactionTypeDto countOfReactionTypeDto) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        modelMapper.map(countOfReactionTypeDto, this);

        return this;
    }
}
