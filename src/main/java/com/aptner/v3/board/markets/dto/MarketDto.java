package com.aptner.v3.board.markets.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class MarketDto {
    @Getter
    @SuperBuilder
    public static class MarketRequest extends CommonPostDto.CommonRequest {
        private String type;
        private String status;
    }

    @Getter
    @NoArgsConstructor
    public static class MarketResponse extends CommonPostDto.CommonResponse {
        private long id;
        private String type;
        private String status;
    }
}
