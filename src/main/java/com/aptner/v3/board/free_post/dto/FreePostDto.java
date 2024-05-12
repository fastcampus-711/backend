package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

public class FreePostDto {
    @Getter
    public static class CreateRequest extends CommonPostDto.CreateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;

        public FreePost toEntity() {
            return new FreePost(getTitle(), getContent(), blindAt, blindBy);
        }
    }

    @Getter
    public static class UpdateRequest extends CommonPostDto.UpdateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;
    }
}
