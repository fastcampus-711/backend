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
    private final Class<?> clazz;
    private final String dtype;

    CategoryName(String uri, Class<?> clazz) {
        this.URI = uri;
        this.clazz = clazz;
        this.dtype = clazz.getSimpleName();
    }
}