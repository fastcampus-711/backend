package com.aptner.v3.reaction.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
@Getter
public class Reactions {
    @ColumnDefault(value = "0")
    private long countOfGood;

    @ColumnDefault(value = "0")
    private long countOfBad;

    public Reactions() {
    }

    public Reactions(long countOfGood, long countOfBad) {
        this.countOfGood = countOfGood;
        this.countOfBad = countOfBad;
    }

    public void blindColumns() {
        this.countOfGood = 0;
        this.countOfBad = 0;
    }
}
