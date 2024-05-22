package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MarketDto {
    @Getter
    public static class Request extends CommonPostDto.Request {
        private String type;
        private String status;
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private long id;
        private String type;
        private String status;
    }
}
