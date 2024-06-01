package com.aptner.v3.board.market;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.market.dto.MarketStatusDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "나눔 게시판")
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<
        Market,
        MarketDto,
        MarketDto.MarketRequest,
        MarketDto.MarketResponse> {
    MarketService marketService;

    public MarketController(@Qualifier("marketService") MarketService marketService, PaginationService paginationService) {
        super(marketService, paginationService);
        this.marketService = marketService;
    }

    @GetMapping("/status")
    @Operation(summary = "상태 목록")
    public ApiResponse<?> getStatusList() {
        return ResponseUtil.ok(MarketStatusDto.MarketStatusResponse.toList());
    }

    @PostMapping("/status")
    @Operation(summary = "상태 등록")
    public ApiResponse<?> setStatus(
            @RequestBody MarketStatusDto.MarketStatusRequest request
            , @AuthenticationPrincipal CustomUserDetails user) {

        MarketDto dto = MarketDto.of(
                getBoardGroup(),
                user.toDto(),
                MarketDto.MarketRequest.of(request.getPostId(), null)
        );

        return ResponseUtil.ok(marketService.setStatus(dto).toResponse());
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.MARKETS;
    }

}
