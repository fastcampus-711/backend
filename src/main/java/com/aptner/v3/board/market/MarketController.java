package com.aptner.v3.board.market;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.market.dto.MarketDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "나눔 게시판")
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<
        Market,
        MarketDto,
        MarketDto.MarketRequest,
        MarketDto.MarketResponse> {
    public MarketController(CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> commonPostService, PaginationService paginationService) {
        super(commonPostService, paginationService);
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.MARKETS;
    }

}
