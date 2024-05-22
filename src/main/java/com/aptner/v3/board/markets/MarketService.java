package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.markets.dto.MarketDto;
import org.springframework.stereotype.Service;

@Service
public class MarketService extends CommonPostService<Market, MarketDto.MarketRequest, MarketDto.MarketResponse> {
    public MarketService(CommonPostRepository<Market> commonPostRepository) {
        super(commonPostRepository);
    }
}
