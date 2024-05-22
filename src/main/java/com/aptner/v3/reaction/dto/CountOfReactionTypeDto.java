package com.aptner.v3.reaction.dto;

import com.aptner.v3.reaction.domain.ReactionColumns;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CountOfReactionTypeDto {
    private ReactionColumns reactionColumns;

    public CountOfReactionTypeDto(long countReactionTypeGood, long countReactionTypeBad) {
       this.reactionColumns = new ReactionColumns(countReactionTypeGood, countReactionTypeBad);
    }
}
