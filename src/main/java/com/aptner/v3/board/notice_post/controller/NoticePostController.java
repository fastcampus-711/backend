package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.notice_post.NoticePostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name="공지 사항")
@RequestMapping("/boards/notices")
public class NoticePostController extends CommonPostController<
        NoticePost,
        NoticePostDto,
        NoticePostDto.NoticeRequest,
        NoticePostDto.NoticeResponse> {
    NoticePostService noticePostService;
    public NoticePostController(@Qualifier("noticePostService") NoticePostService noticePostService, PaginationService paginationService) {
        super(noticePostService, paginationService);
        this.noticePostService = (NoticePostService) noticePostService;
    }

    @GetMapping("/duty")
    @Operation(summary = "의무 공개")
    public ApiResponse<?> getDutyPostListByCategoryId(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "status", required = false) Status status,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {
        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        Page<NoticePostDto> posts = noticePostService.getPostList(boardGroup, categoryId, keyword, status, null, true, pageable);

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                posts.map(p -> p.toResponse()),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), posts.getTotalPages())
        )));
    }

    @GetMapping("/event")
    @Operation(summary = "일정 조회")
    public ResponseEntity<?> getPostListByCategoryId(@RequestParam(name = "start") LocalDate start,
                                                     @RequestParam(name = "end") LocalDate end
    ) {
        List<NoticePost> searched = noticePostService.getNoticePostsByScheduleRange(start, end, getBoardGroup());
        // 아파트너사와 동일한 형태 응답값
        List<NoticePostDto.NoticeResponse> response = searched.stream().map(p -> p.toDto().toResponse()).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    public BoardGroup getBoardGroup() {
        return BoardGroup.NOTICES;
    }
}
