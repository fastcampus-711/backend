package com.aptner.v3.board.complain;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.complain.dto.ComplainDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="민원 게시판")
@RequestMapping("/boards/complains")
public class ComplainController extends CommonPostController<
        Complain,
        ComplainDto,
        ComplainDto.ComplainRequest,
        ComplainDto.ComplainResponse> {
    ComplainService complainService;
    public ComplainController(@Qualifier("complainService") ComplainService complainService, PaginationService paginationService) {
        super(complainService, paginationService);
        this.complainService = complainService;
    }

    @GetMapping("/my")
    @Operation(summary = "나의 민원")
    public ApiResponse<?> getMyPostListByCategoryId(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                    @RequestParam(name = "keyword", required = false) String keyword,
                                                    @RequestParam(name = "status", required = false) Status status,
                                                    @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                    @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort,
                                                    @AuthenticationPrincipal CustomUserDetails user
    ) {
        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        Page<ComplainDto> posts = commonPostService.getPostList(boardGroup, categoryId, keyword, status, user.getId(), pageable);

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                posts.map(p -> p.toResponse()),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), posts.getTotalPages())
        )));
    }

    @Override
    public BoardGroup getBoardGroup() {
        return  BoardGroup.COMPLAINT;
    }
}
