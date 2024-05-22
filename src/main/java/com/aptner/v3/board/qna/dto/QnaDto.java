package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
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
