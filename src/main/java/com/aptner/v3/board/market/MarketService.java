package com.aptner.v3.board.market;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Qualifier("marketService")
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    private final MarketRepository marketRepository;

    public MarketService(MemberRepository memberRepository,
                         CategoryRepository categoryRepository,
                         @Qualifier("marketRepository") MarketRepository marketRepository,
                         ReactionRepository<PostReaction> postReactionRepository
    ) {
        super(memberRepository, categoryRepository, marketRepository, postReactionRepository);
        this.marketRepository = marketRepository;

    }

    public MarketDto setStatus(MarketDto dto) {
        Market market = verifyPost(dto);
        // update
        market.setStatus(dto.getStatus());
        marketRepository.flush();
        return market.toDto();
    }

    @Override
    public MarketDto updatePost(MarketDto dto) {

        log.debug("Market.updatePost : dto {}", dto);
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
        if (dto.getStatus() != null) {
            post.setStatus(dto.getStatus());
        }
        if (dto.getPrice() != null) {
            post.setPrice(dto.getPrice());
        }

        marketRepository.flush();
        log.debug("updatePost : {}", post);

        MarketDto postDto = (MarketDto) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

}
