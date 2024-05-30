package com.aptner.v3.board.category;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.board.qna.dto.QnaDto;
import lombok.Getter;

@Getter
public enum CategoryCode {
    공통("", CommonPost.class, CommonPostDto.CommonPostRequest.class, CommonPostDto.CommonPostResponse.class),
    공지사항("notices", NoticePost.class, NoticePostDto.NoticeRequest.class, NoticePostDto.NoticeResponse.class),
    자유게시판("frees", FreePost.class, FreePostDto.FreePostRequest.class, FreePostDto.FreePostResponse.class),
    QNA("qnas", Qna.class, QnaDto.QnaRequest.class, QnaDto.QnaResponse.class),
    나눔장터("markets", Market.class, MarketDto.MarketRequest.class, MarketDto.MarketResponse.class),
    민원게시판("complains", Complain.class, ComplainDto.ComplainRequest.class, ComplainDto.ComplainResponse.class);

    private final String URI;
    private final Class<? extends CommonPost> domain;
    private final String dtype;
    private final Class<? extends CommonPostDto.CommonPostRequest> dtoForRequest;

    private final Class<? extends CommonPostDto.CommonPostResponse> dtoForResponse;

    CategoryCode(String uri,
                 Class<? extends CommonPost> domain,
                 Class<? extends CommonPostDto.CommonPostRequest> dtoForRequest,
                 Class<? extends CommonPostDto.CommonPostResponse> dtoForResponse) {
        this.URI = uri;
        this.domain = domain;
        this.dtoForRequest = dtoForRequest;
        this.dtoForResponse = dtoForResponse;
        this.dtype = domain.getSimpleName();
    }
}