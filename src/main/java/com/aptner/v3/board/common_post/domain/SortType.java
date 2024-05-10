package com.aptner.v3.board.common_post.domain;

import lombok.Getter;

@Getter
public enum SortType {
    LIKED("hits"), RECENT("createdAt");

    private final String columnName;

    SortType(String columnName) {
        this.columnName = columnName;
    }
}
