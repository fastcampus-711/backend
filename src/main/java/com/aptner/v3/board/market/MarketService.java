package com.aptner.v3.board.market;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    private final CommonPostRepository<Market> commonPostRepository;

    public MarketService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Market> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

}
