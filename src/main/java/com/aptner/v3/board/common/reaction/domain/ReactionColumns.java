package com.aptner.v3.board.common.reaction.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ReactionColumns {

    private long countReactionTypeGood;

    private long countReactionTypeBad;

    public ReactionColumns() {
    }

    public ReactionColumns(long countReactionTypeGood, long countBad) {
        this.countReactionTypeGood = countReactionTypeGood;
        this.countReactionTypeBad = countBad;
    }
}
