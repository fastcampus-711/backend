package com.aptner.v3.board.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardGroup {
    ALL(1L, "전체", 0L, "CommonPost"),
    NOTICES(2L, "공지사항", 7L, "NoticePost"),
    COMPLAINT(3L, "전체민원", 15L, "ComplaintPost"),
    MYCOMPLAINT(3L, "나의민원", 16L, "ComplaintPost"),
    FREES(4L, "자유게시판", 11L, "FreePost"),
    QNAS(5L, "QNA", 13L, "QnaPost"),
    MARKETS(6L, "나눔장터", 12L, "MarketPost");
    private final Long id;
    private final String domain;
    private final Long menuId;
    private final String table;

    public static BoardGroup getById(Long id) {
        for (BoardGroup group : values()) {
            if (group.id.equals(id)) {
                return group;
            }
        }
        throw new IllegalArgumentException("No BoardGroup found with id: " + id);
    }
}