package com.aptner.v3.board.category;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryName {
    공통("", CommonPost.class),
    공지사항("notices", NoticePost.class),
    자유게시판("frees", FreePost.class);

    private final String URI;
    private final Class<?> domain;
    private final String dtype;
    private final Class<?> dtoForResponse;

    CategoryName(String uri, Class<?> domain, Class<?> dtoForResponse) {
        this.URI = uri;
        this.domain = domain;
        this.dtoForResponse = dtoForResponse;
        this.dtype = domain.getSimpleName();
    }
}