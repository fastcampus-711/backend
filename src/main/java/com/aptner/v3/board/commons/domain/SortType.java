package com.aptner.v3.board.commons.domain;

import lombok.Getter;

@Getter
public enum SortType {
    LIKE("hits"), RECENT("createdAt");

    private final String columnName;

    SortType(String columnName) {
        this.columnName = columnName;
    }
}
