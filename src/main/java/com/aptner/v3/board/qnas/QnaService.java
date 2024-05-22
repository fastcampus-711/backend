package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.markets.Market;
import com.aptner.v3.board.markets.dto.MarketDto;

public class QnaService extends CommonPostService<Market, MarketDto.Request, MarketDto.Response> {
    public QnaService(CommonPostRepository<Market> commonPostRepository) {
        super(commonPostRepository);
    }
}
