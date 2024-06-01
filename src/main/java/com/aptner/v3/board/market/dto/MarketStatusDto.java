package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.market.MarketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class MarketStatusDto {

    Long postId;
    MarketStatus status;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarketStatusRequest {
        Long postId;
        MarketStatus status;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarketStatusResponse {
        private String code;
        private String description;
        public static MarketStatusDto.MarketStatusResponse from(MarketStatus status) {
            return new MarketStatusDto.MarketStatusResponse(status.name(), status.getDescription());
        }

        public static List<MarketStatusResponse> toList() {
            return Arrays.stream(MarketStatus.values())
                    .map(MarketStatusResponse::from)
                    .collect(Collectors.toList());
        }

    }
}