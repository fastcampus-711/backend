package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.complain.ComplainService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.board.free_post.FreePostService;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.market.MarketService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.notice_post.NoticePostService;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.QnaService;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/boards")
public class SearchController {

    private final NoticePostService noticePostService;
    private final FreePostService freePostService;
    private final MarketService marketService;
    private final QnaService qnaService;
    private final ComplainService complainService;

    @Autowired
    public SearchController(NoticePostService noticePostService,
                            FreePostService freePostService,
                            MarketService marketService,
                            QnaService qnaService,
                            ComplainService complainService) {
        this.noticePostService = noticePostService;
        this.freePostService = freePostService;
        this.marketService = marketService;
        this.qnaService = qnaService;
        this.complainService = complainService;
    }

    @GetMapping("/search")
    @Operation(summary = "통합검색")
    public ApiResponse<?> getPostListByCategoryId(@RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "status", required = false) Status status,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "4") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        Map<BoardGroup, Map<String, Object>> resultMap = new HashMap<>();

        Page<NoticePostDto> noticePostList = noticePostService.getPostList(BoardGroup.NOTICES, null, keyword, null, null, pageable);
        resultMap.put(BoardGroup.NOTICES, Map.of(
                "count", noticePostList != null ? noticePostList.getTotalElements() : 0,
                "data", noticePostList != null ? noticePostList.map(p->p.toResponse()) : List.of()
        ));

        Page<FreePostDto> freePostList = freePostService.getPostListWithoutHotPost(BoardGroup.FREES, null, keyword, null, null, pageable);
        resultMap.put(BoardGroup.FREES, Map.of(
                "count", freePostList != null ? freePostList.getTotalElements() : 0,
                "data", freePostList != null ? freePostList.map(p->p.toResponse()) : List.of()
        ));

        Page<MarketDto> marketPostList = marketService.getPostList(BoardGroup.MARKETS, null, keyword, null, null, pageable);
        resultMap.put(BoardGroup.MARKETS, Map.of(
                "count", marketPostList != null ? marketPostList.getTotalElements() : 0,
                "data", marketPostList != null ? marketPostList.map(p->p.toResponse()) : List.of()
        ));

        Page<QnaDto> qnaPostList = qnaService.getPostList(BoardGroup.QNAS, null, keyword, null, null, pageable);
        resultMap.put(BoardGroup.QNAS, Map.of(
                "count", qnaPostList != null ? qnaPostList.getTotalElements() : 0,
                "data", qnaPostList != null ? qnaPostList.map(p->p.toResponse()) : List.of()
        ));

        Page<ComplainDto> complaintPostList = complainService.getPostList(BoardGroup.COMPLAINT, null, keyword, null, null, pageable);
        resultMap.put(BoardGroup.COMPLAINT, Map.of(
                "count", complaintPostList != null ? complaintPostList.getTotalElements() : 0,
                "data", complaintPostList != null ? complaintPostList.map(p->p.toResponse()) : List.of()
        ));
        return ResponseUtil.ok(resultMap);
    }

}
