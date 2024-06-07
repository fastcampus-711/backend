package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.member.dto.MemberDto;

import java.time.LocalDateTime;

public class NoticeScheduleDto {
    LocalDateTime scheduleStartAt;
    LocalDateTime scheduleEndAt;
    BoardGroup boardGroup;
    MemberDto memberDto;

    public static class NoticeScheduleRequest {
        private LocalDateTime scheduleStartAt;
        private LocalDateTime scheduleEndAt;
    }

    public static class NoticeScheduleResponse {
        private Long id;
        private String title;
        private LocalDateTime scheduleStartAt;
        private LocalDateTime scheduleEndAt;
    }
}
