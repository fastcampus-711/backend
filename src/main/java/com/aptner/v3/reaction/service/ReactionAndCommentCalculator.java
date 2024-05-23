package com.aptner.v3.reaction.service;

import com.aptner.v3.reaction.dto.CountOfReactionTypeDto;

import static com.aptner.v3.CommunityApplication.modelMapper;

public interface ReactionAndCommentCalculator {
    default ReactionAndCommentCalculator updateCountOfReactions(CountOfReactionTypeDto countOfReactionTypeDto) {

        modelMapper().map(countOfReactionTypeDto, this);

        return this;
    }
}
