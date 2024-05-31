package com.aptner.v3.board.market;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarketStatus {
    //MARKET
    SALE("판매중"),
    RESERVED("예약중"),
    SOLD_OUT("판매완료");

    private final String description;
}
