package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import org.springframework.stereotype.Service;

@Service
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.Request, MarketDto.Response> {
    private final CommonPostRepository<Market> commonPostRepository;
    public MarketService(CommonPostRepository<Market> commonPostRepository) {
        super(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

    public MarketDto.Response createPost(MarketDto.Request requestDto) {
        if (!requestDto.getImageUrls().isEmpty()) {
            Market entity = requestDto.toEntity(requestDto.getImageUrls());
            return new MarketDto.Response(commonPostRepository.save(entity));
        }
        return new MarketDto.Response(commonPostRepository.save(requestDto.toEntity()));
    }

    /*public MarketDto.Response updatePost(long postId,MarketDto.Request requestDto) {
        Market entity = commonPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        entity.update(requestDto);
        return new MarketDto.Response(commonPostRepository.save(entity));
    }

    public long deletePost(long postId) {
        Market entity = commonPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        commonPostRepository.deleteById(postId);
        return entity.getId();
    }*/
}
