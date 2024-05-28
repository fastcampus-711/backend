package com.aptner.v3.board.common.reaction.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ReactionColumns {
    private long countReactionTypeGood;

    private long countReactionTypeBad;

    public ReactionColumns() {
    }

    public ReactionColumns(long countReactionTypeGood, long countReactionTypeBad) {
        this.countReactionTypeGood = countReactionTypeGood;
        this.countReactionTypeBad = countReactionTypeBad;
    }
}
