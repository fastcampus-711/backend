package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.CommonPostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("marketRepository")
public interface MarketRepository extends CommonPostRepository<Market> {
    Page<Market> findByDtypeAndStatus(String dtype, MarketStatus status, Pageable pageable);
    Page<Market> findByDtypeAndCategoryIdAndStatus(String dtype, Long categoryId, MarketStatus status, Pageable pageable);

}
