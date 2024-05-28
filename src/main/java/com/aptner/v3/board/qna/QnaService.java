package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.member.repository.MemberRepository;

public class QnaService extends CommonPostService<Market, MarketDto, MarketDto.MarketReqeust, MarketDto.MarketResponse> {

    public QnaService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Market> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
    }
}
