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
    공통("", CommonPost.class, CommonPostDto.Response.class),
    공지사항("notices", NoticePost.class, NoticePostDto.Response.class),
    자유게시판("frees", FreePost.class, FreePostDto.Response.class),
    QNA("qnas", Qna.class, QnaDto.Response.class),
    나눔장터("markets", Market.class, MarketDto.Response.class),
    민원게시판("complains", Complain.class, ComplainDto.Response.class);

    private final String URI;
    private final Class<?> domain;
    private final String dtype;
    private final Class<? extends CommonPostDto.Response> dtoForResponse;

    CategoryCode(String uri, Class<?> domain, Class<? extends CommonPostDto.Response> dtoForResponse) {
        this.URI = uri;
        this.domain = domain;
        this.dtoForResponse = dtoForResponse;
        this.dtype = domain.getSimpleName();
    }
}