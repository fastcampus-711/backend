package com.aptner.v3.board.market;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.aptner.v3.global.error.ErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@Qualifier("marketService")
public class MarketService extends CommonPostService<Market, MarketDto, MarketDto.MarketRequest, MarketDto.MarketResponse> {

    private final MarketRepository marketRepository;

    public MarketService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Market> commonPostRepository, @Qualifier("marketRepository") MarketRepository marketRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
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
        if (dto.getStatus() != null)
            post.setStatus(dto.getStatus());

        if (dto.getPrice() != null)
            post.setPrice(dto.getPrice());

        marketRepository.flush();
        log.debug("updatePost : {}", post);

        MarketDto postDto = (MarketDto) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

    @Override
    protected Market verifyPost(MarketDto dto) {
        if (dto.getId() == null) {
            log.error("POST ID 없음");
            throw new PostException(INVALID_REQUEST);
        }
        // exists

        Market post = marketRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        // 자신이 작성한 글이 아닌 경우
        if (!post.getMember().getId().equals(dto.getMemberDto().getId())) {
            log.error("POST 저장에 MEMBER ID 가 로그인 유저와 상이함");
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }

        // Board 속한 게시글 수정/삭제
        if (StringUtils.isNotEmpty(post.getDtype())
                && !post.getDtype().equals(dto.getBoardGroup().getTable())) {
            log.error("속한 카테고리가 아님: {} | {}", post.getDtype(), dto.getBoardGroup().getTable());
            throw new PostException(INVALID_REQUEST);
        }
        return post;
    }

    @Override
    public Page<Market> findByDtypeAndStatus(BoardGroup boardGroup, Status status, Pageable pageable) {
        return marketRepository.findByDtypeAndStatus(boardGroup, (MarketStatus) status, pageable);
    }

    @Override
    public Page<Market> findByDtypeAndCategoryIdAndStatus(BoardGroup boardGroup, Long categoryId, Status status, Pageable pageable) {
        return marketRepository.findByDtypeAndCategoryIdAndStatus(boardGroup.getTable(), categoryId, (MarketStatus) status, pageable);
    }
}
