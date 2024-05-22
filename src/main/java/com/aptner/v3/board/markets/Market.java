package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("MARKETS")
public class Market extends CommonPost {

    public enum Status {
        RESERVED,
        SELLING,
        SOLD
    }

    /* 상태값 */
    @Enumerated(EnumType.STRING)
    private Status status = Market.Status.SELLING;

    /* 가격 */
    private int price;

}
