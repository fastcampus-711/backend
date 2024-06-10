package com.aptner.v3.board.common.report;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.common.report.dto.ReportDto;
import com.aptner.v3.board.common.report.service.ReportService;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "신고")
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/post")
    @Operation(summary = "게시글 신고")
    public ApiResponse<?> savePostReport(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReportDto.ReportRequest reportDto) {

        reportDto.setUserId(user.getId());
        return ResponseUtil.ok(reportService.savePostReport(reportDto));
    }

    @PostMapping("/comment")
    @Operation(summary = "댓글 신고")
    public ApiResponse<?> saveCommentReport(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReportDto.ReportRequest reportDto) {

        reportDto.setUserId(user.getId());
        return ResponseUtil.ok(reportService.saveCommentReport(reportDto));
    }
}
