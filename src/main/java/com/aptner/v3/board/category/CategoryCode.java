package com.aptner.v3.board.category;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
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
import lombok.RequiredArgsConstructor;

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
    private final Class<?> dtoForResponse;

    CategoryCode(String uri, Class<?> domain, Class<?> dtoForResponse) {
        this.URI = uri;
        this.domain = domain;
        this.dtoForResponse = dtoForResponse;
        this.dtype = domain.getSimpleName();
    }
}