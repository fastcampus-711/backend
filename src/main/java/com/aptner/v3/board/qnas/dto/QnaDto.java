package com.aptner.v3.board.qnas.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class QnaDto {
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
