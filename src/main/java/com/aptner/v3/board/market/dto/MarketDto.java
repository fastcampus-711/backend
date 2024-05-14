package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;

public class MarketDto {
    public static class Request extends CommonPostDto.Request {
        private String type;
        private String status;
    }

    public static class Response extends CommonPostDto.Response {
        private long id;
        private String type;
        private String status;
    }
}
