package com.aptner.v3.board.qna;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.board.qna.dto.QnaStatusDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "QNA 게시판")
@RequestMapping("/boards/qna")
public class QnaController extends CommonPostController<
        Qna,
        QnaDto,
        QnaDto.QnaRequest,
        QnaDto.QnaResponse> {

    QnaService qnaService;
    public QnaController(@Qualifier("qnaService") QnaService qnaService, PaginationService paginationService) {
        super(qnaService, paginationService);
        this.qnaService = qnaService;
    }

    @GetMapping("/status")
    @Operation(summary = "상태 목록")
    public ApiResponse<?> getStatusList() {
        return ResponseUtil.ok(QnaStatusDto.QnaStatusResponse.toList());
    }

    @PostMapping("/status")
    @Operation(summary = "상태 등록")
    public ApiResponse<?> setStatus(
            @RequestBody QnaStatusDto.QnaStatusRequest request
            , @AuthenticationPrincipal CustomUserDetails user) {

        QnaDto dto = QnaDto.of(
                getBoardGroup(),
                user.toDto(),
                QnaDto.QnaRequest.of(request.getPostId(), null)
        );
        return ResponseUtil.ok(qnaService.setStatus(dto).toResponse());
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.QNAS;
    }
}
