package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;

public class QnaDto {
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
