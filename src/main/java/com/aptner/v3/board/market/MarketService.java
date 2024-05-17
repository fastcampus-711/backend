package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import org.springframework.stereotype.Service;

@Service
public class MarketService extends CommonPostService<Market, MarketDto.Request, MarketDto.Response> {
    public MarketService(CommonPostRepository<Market> commonPostRepository) {
        super(commonPostRepository);
    }
}