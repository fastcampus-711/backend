package com.aptner.v3.board.qna;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.board.qna.dto.QnaStatusResponse;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    public QnaController(CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.QnaResponse> commonPostService, PaginationService paginationService) {
        super(commonPostService, paginationService);
    }

    @PostMapping()
    @Operation(summary = "게시글 등록")
    public ApiResponse<?> createPost(
            @RequestBody QnaDto.QnaRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return super.createPost(request, user);
    }


    @GetMapping("/status")
    @Operation(summary = "상태 목록")
    public ApiResponse<?> getStatusList() {
        return ResponseUtil.ok(QnaStatusResponse.toList());
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.QNAS;
    }
}
