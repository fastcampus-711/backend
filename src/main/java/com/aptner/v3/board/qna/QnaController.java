package com.aptner.v3.board.qna;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.board.qna.dto.QnaStatusDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/comment")
    @Operation(summary = "게시글 별 검색 (댓글 포함)")
    public ApiResponse<?> getPostListByCategoryIdWithComment(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "status", required = false) Status status,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {
        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        Page<QnaDto> posts = null;
        if (keyword != null) {
            // 키워드 검색
            log.debug("keyword search : boardGroup: {}, categoryId: {}, keyword: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, keyword, limit, page, sort);
            posts = qnaService.getPostListByCategoryIdAndTitle(boardGroup, categoryId, keyword, pageable);
        } else {
            // 카테고리 - 분류 검색
            log.debug("category search : boardGroup: {},  categoryId: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, limit, page, sort);
            posts = qnaService.getPostListByCategoryId(boardGroup, categoryId, status, pageable);
        }

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                posts.map(p -> p.toResponseWithComment()),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), posts.getTotalPages())
        )));
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
