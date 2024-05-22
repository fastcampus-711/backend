package com.aptner.v3.board.frees.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.frees.domain.FreePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class FreePostDto {
    @Getter
    @SuperBuilder
    public static class FreeCommonRequest extends CommonPostDto.CommonRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;

        public FreePost toEntity() {
            return new FreePost(getTitle(), getContent(), blindAt, blindBy);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FreeCommonResponse extends CommonPostDto.CommonResponse {
        private LocalDateTime blindAt;
        private String blindBy;

        public <E extends CommonPost> FreeCommonResponse(E entity) {
            super(entity);
        }
    }
}
