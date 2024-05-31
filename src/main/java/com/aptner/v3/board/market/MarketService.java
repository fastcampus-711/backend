package com.aptner.v3.board.market;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Qualifier("marketService")
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    MarketRepository marketRepository;

    public MarketService(MemberRepository memberRepository, CategoryRepository categoryRepository, @Qualifier("marketRepository") MarketRepository marketRepository) {
        super(memberRepository, categoryRepository, marketRepository);
        this.marketRepository = marketRepository;
    }

    @Override
    public Page<Market> findByDtypeAndStatus(BoardGroup boardGroup, Status status, Pageable pageable) {
        return marketRepository.findByDtypeAndStatus(boardGroup, (MarketStatus) status, pageable);
    }

    @Override
    public Page<Market> findByDtypeAndCategoryIdAndStatus(BoardGroup boardGroup, Long categoryId, Status status, Pageable pageable) {
        return marketRepository.findByDtypeAndCategoryIdAndStatus(boardGroup.getTable(), categoryId, (MarketStatus) status, pageable);
    }

    @Override
    public MarketDto updatePost(MarketDto dto) {

        log.debug("updatePost : dto {}", dto);
        Member member = verifyMember(dto);
        Category category = verifyCategory(dto);
        Market post = verifyPost(dto);

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getImageUrls() != null) {
            post.setImageUrls(dto.getImageUrls());
        }

        marketRepository.flush();
        log.debug("updatePost : {}", post);

        MarketDto postDto = (MarketDto) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

}
