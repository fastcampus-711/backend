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
    private Long id;
    private String domain;
    private Long menuId;
    private String table;

    BoardGroup(){}

    public static BoardGroup getById(Long id) {
        if (id == null) return null;
        for (BoardGroup group : values()) {
            if (group.id.equals(id)) {
                return group;
            }
        }
        return null;
    }

    public static BoardGroup getByTable(String table) {
        if (table == null) return null;
        for (BoardGroup group : values()) {
            if (group.table.equals(table)) {
                return group;
            }
        }
        return null;
    }

}