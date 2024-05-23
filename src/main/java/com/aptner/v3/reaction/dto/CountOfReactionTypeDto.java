package com.aptner.v3.reaction.dto;

import com.aptner.v3.reaction.domain.Reactions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CountOfReactionTypeDto {
    private Reactions reactions;

    public CountOfReactionTypeDto(long countReactionTypeGood, long countReactionTypeBad) {
       this.reactions = new Reactions(countReactionTypeGood, countReactionTypeBad);
    }
}
