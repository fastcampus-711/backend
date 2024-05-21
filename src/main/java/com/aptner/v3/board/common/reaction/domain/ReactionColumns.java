package com.aptner.v3.board.common.reaction.domain;

import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
public abstract class ReactionColumns extends BaseTimeEntity {
    @ColumnDefault(value = "0")
    private long countReactionTypeGood;

    @ColumnDefault(value = "0")
    private long countReactionTypeBad;

    public ReactionColumns updateCountOfReactions(long countReactionTypeGood, long countReactionTypeBad) {
        this.countReactionTypeGood = countReactionTypeGood;
        this.countReactionTypeBad = countReactionTypeBad;

        return this;
    }
}
