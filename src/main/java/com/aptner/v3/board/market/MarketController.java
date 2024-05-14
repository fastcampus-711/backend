package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<Market, MarketDto.Request, MarketDto.Response> {
    public MarketController(CommonPostService<Market, MarketDto.Request, MarketDto.Response> commonPostService) {
        super(commonPostService);
    }
}
