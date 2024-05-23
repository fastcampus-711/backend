package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FreePostDto {
    @Getter
    public static class Request extends CommonPostDto.Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private LocalDateTime blindAt;
        private String blindBy;
    }
}
