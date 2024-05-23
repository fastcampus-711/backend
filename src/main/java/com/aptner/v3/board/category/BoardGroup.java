package com.aptner.v3.board.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardGroup {
    ALL(1L, "전체"),
    NOTICES(2L, "공지사항"),
    COMPLAINT(3L, "전체민원"),
    MYCOMPLAINT(3L, "나의민원"),
    FREES(4L, "자유게시판"),
    QNAS(5L, "QNA"),
    MARKETS(6L, "나눔장터");
    private final Long id;
    private final String domain;

    public static BoardGroup getById(Long id) {
        for (BoardGroup group : values()) {
            if (group.id.equals(id)) {
                return group;
            }
        }
        throw new IllegalArgumentException("No BoardGroup found with id: " + id);
    }
}