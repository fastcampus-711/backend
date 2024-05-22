package com.aptner.v3.category;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.complains.Complain;
import com.aptner.v3.board.complains.dto.ComplainDto;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.frees.dto.FreePostDto;
import com.aptner.v3.board.markets.Market;
import com.aptner.v3.board.markets.dto.MarketDto;
import com.aptner.v3.board.notices.domain.NoticePost;
import com.aptner.v3.board.notices.dto.NoticePostDto;
import com.aptner.v3.board.qnas.Qna;
import com.aptner.v3.board.qnas.dto.QnaDto;
import lombok.Getter;

@Getter
public enum CategoryCode {
    공통("", CommonPost.class, CommonPostDto.CommonResponse.class),
    공지사항("notices", NoticePost.class, NoticePostDto.CommonResponse.class),
    자유게시판("frees", FreePost.class, FreePostDto.FreeCommonResponse.class),
    QNA("qnas", Qna.class, QnaDto.QnaResponse.class),
    나눔장터("markets", Market.class, MarketDto.MarketResponse.class),
    민원게시판("complains", Complain.class, ComplainDto.ComplainResponse.class);

    private final String URI;
    private final Class<?> domain;
    private final String dtype;
    private final Class<? extends CommonPostDto.CommonResponse> dtoForResponse;

    CategoryCode(String uri, Class<?> domain, Class<? extends CommonPostDto.CommonResponse> dtoForResponse) {
        this.URI = uri;
        this.domain = domain;
        this.dtoForResponse = dtoForResponse;
        this.dtype = domain.getSimpleName();
    }
}