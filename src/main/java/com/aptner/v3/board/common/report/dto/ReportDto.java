package com.aptner.v3.board.common.report.dto;

import com.aptner.v3.board.common.report.domain.Report;
import com.aptner.v3.board.common.report.domain.ReportColumns;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ReportDto {
    @Getter
    public static class ReportRequest {

        @Setter
        private long userId;
        @NotNull
        private long targetId;
        @NotNull
        private ReportColumns reportColumns;
    }
    public static Report toEntity(ReportRequest request) {
        return Report.builder()
                .userId(request.getUserId())
                .targetId(request.getTargetId())
                .reportColumns(request.getReportColumns())
                .build();
    }
    @Getter
    @AllArgsConstructor
    public static class ReportResponse {

        @Setter
        private long userId;
        @NotNull
        private long targetId;
        @NotNull
        private ReportColumns reportColumns;

    }
}