package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.markets.dto.MarketDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="나눔 게시판")
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<Market, MarketDto.MarketRequest, MarketDto.MarketResponse> {
    public MarketController(CommonPostService<Market, MarketDto.MarketRequest, MarketDto.MarketResponse> commonPostService) {
        super(commonPostService);
    }
}
