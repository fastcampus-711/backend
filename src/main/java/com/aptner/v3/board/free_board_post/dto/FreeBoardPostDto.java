package com.aptner.v3.board.free_board_post.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.free_board_post.domain.FreeBoardPost;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

public class FreeBoardPostDto {
    @Getter
    public static class CreateRequest extends CommonPostDto.CreateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;

        public FreeBoardPost toEntity() {
            return new FreeBoardPost(getTitle(), getContent(), blindAt, blindBy);
        }
    }

    @Getter
    public static class UpdateRequest extends CommonPostDto.UpdateRequest {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;
    }
}
