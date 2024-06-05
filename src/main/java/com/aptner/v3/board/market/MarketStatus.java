package com.aptner.v3.board.market;

import com.aptner.v3.board.qna.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarketStatus implements Status {
    //MARKET
    SALE("SALE","판매중"),
    RESERVED("RESERVED","예약중"),
    SOLD_OUT("SOLD_OUT","판매완료");

    private final String code;
    private final String description;
}
