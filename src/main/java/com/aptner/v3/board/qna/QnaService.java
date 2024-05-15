package com.aptner.v3.board.qna;

import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.dto.MarketDto;

public class QnaService extends CommonPostService<Market, MarketDto.Request, MarketDto.Response> {
    public QnaService(CommonPostRepository<Market> commonPostRepository) {
        super(commonPostRepository);
    }
}
