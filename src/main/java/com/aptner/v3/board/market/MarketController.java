package com.aptner.v3.board.market;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "나눔 게시판")
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    private final MarketService marketService;

    public MarketController(CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> commonPostService,
                            PaginationService paginationService,
                            MarketService marketService) {
        super(commonPostService, paginationService);
        this.marketService = marketService;
    }




    //status 별 게시글 조회
    @GetMapping("/test")
    public List<MarketDto.MarketResponse> getPostListByCategoryIdAndStatusAndTitle(@RequestParam(name = "category-id") Long categoryId,
                                                                                   @RequestParam(name = "status") MarketStatus status,
                                                                                   @RequestParam(name = "keyword", required = false) String keyword,
                                                                                   @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                                                   @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                                   @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort) {
        /*if (keyword == null)
            return marketService.getMarketListByStatus(categoryId, status, limit, page, sort);
        else
            return marketService.getMarketListByStatusAndKeyword(categoryId, status, keyword, limit, page, sort);*/
        System.out.println("@@@@@@@@@status"+ status.toString());
        return null;
    }

    @GetMapping("/status")
    @Operation(summary = "상태 목록")
    public ApiResponse<?> getStatusList() {
        return ResponseUtil.ok("ok");
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.MARKETS;
    }

}
