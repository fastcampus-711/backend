package com.aptner.v3.board.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryName {
    공지사항("notices"),
    자유게시판("free-boards");

    private final String tableName;
}