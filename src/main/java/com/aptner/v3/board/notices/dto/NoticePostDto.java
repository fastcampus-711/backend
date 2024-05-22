package com.aptner.v3.board.notices.dto;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.notices.domain.NoticePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class NoticePostDto {

    @Getter
    @SuperBuilder
    public static class CommonRequest extends CommonPostDto.CommonRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postAt;

        public NoticePost toEntity() {
            return new NoticePost(getTitle(), getContent(), postAt);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommonResponse extends CommonPostDto.CommonResponse {
        private LocalDateTime postAt;

        public <E extends CommonPost> CommonResponse(E entity) {
            super(entity);
        }
    }
}
