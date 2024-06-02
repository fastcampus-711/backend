package com.aptner.v3.board.common_post.domain;

import lombok.Getter;

@Getter
public enum SortType {

    LIKE("hits"),
    RECENT("createdAt"),
    ADMIN("isAdminComment");
    private final String columnName;

    SortType(String columnName) {
        this.columnName = columnName;
    }
}
