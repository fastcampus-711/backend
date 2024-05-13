package com.aptner.v3.board.category.dto;

import com.aptner.v3.board.category.Category;

public record CategoryDtoResponse(
        Long id,
        Long parentId,
        String code,
        String name
) {

    public static CategoryDtoResponse of(Long id, String code, String name) {
        return CategoryDtoResponse.of(id, code, name);
    }

    public static CategoryDtoResponse of(Long id, Long menuId, String code, String name) {

        return new CategoryDtoResponse(id, menuId, code, name);
    }

    public static CategoryDtoResponse from(Category category) {

        return CategoryDtoResponse.of(
                category.getId(),
                category.getMenuId(),
                category.getCode(),
                category.getName()
        );
    }
}
