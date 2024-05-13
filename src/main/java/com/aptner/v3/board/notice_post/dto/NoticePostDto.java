package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class NoticePostDto {

    @Getter
    public static class CreateRequest extends CommonPostDto.CreateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postAt;

        public NoticePost toEntity() {
            return new NoticePost(getTitle(), getContent(), postAt);
        }
    }

    @Getter
    public static class UpdateRequest extends CommonPostDto.UpdateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postAt;
    }
}
