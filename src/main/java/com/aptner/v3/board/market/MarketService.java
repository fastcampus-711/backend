package com.aptner.v3.board.market;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("marketService")
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    private final MarketRepository marketRepository;

    public MarketService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Market> commonPostRepository, MarketRepository marketRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
        this.marketRepository = marketRepository;
    }



    /*public List<MarketDto.MarketResponse> getMarketListByStatus(Long categoryId, MarketStatus status, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        Page<Market> list = marketRepository.findAllByCategoryIdAndStatus(categoryId, status, pageable);

        return list.getContent()
                .stream()
                .map(market -> MarketDto.MarketResponse.from(MarketDto.fromMarketEntity(market)))
                .toList();
    }

    public List<MarketDto.MarketResponse> getMarketListByStatusAndKeyword(Long categoryId, MarketStatus status, String keyword, Integer limit, Integer page, SortType sort) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());

        Page<Market> list = marketRepository.findByCategoryIdAndStatusAndTitleContainingIgnoreCase(categoryId, status,keyword,  pageable);

        return list.getContent()
                .stream()
                .map(market -> MarketDto.MarketResponse.from(MarketDto.fromMarketEntity(market)))
                .toList();
    }*/

}
