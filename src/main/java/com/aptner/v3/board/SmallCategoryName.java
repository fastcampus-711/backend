package com.aptner.v3.board;

public enum SmallCategoryName {
    공지사항("notice_post");

    private final String tableName;

    SmallCategoryName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
