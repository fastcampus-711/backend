package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.markets.dto.MarketDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MarketService extends CommonPostService<
        MarketPost,
        MarketDto.MarketRequest,
        MarketDto.MarketResponse,
        MarketDto
        > {

    private final CommonPostRepository<MarketPost> commonPostRepository;

    public MarketService(CommonPostRepository<MarketPost> commonPostRepository, MemberRepository memberRepository, CommonPostRepository<MarketPost> commonPostRepository1) {
        super(commonPostRepository, memberRepository);
        this.commonPostRepository = commonPostRepository1;
    }

    public MarketDto.MarketResponse createPost(MarketDto.MarketRequest request) {
        if (!request.getImageUrls().isEmpty()) {
            MarketPost entity = request.toEntity();
            return new MarketDto.MarketResponse(commonPostRepository.save(entity));
        }
        return new MarketDto.MarketResponse(commonPostRepository.save(request.toEntity()));
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

