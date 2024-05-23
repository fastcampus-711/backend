package com.aptner.v3.category.dto;

import com.aptner.v3.category.BoardGroup;
import com.aptner.v3.category.Category;
import jakarta.validation.constraints.NotBlank;


public record CategoryDtoRequest(
        BoardGroup boardGroup,
        @NotBlank
        String code,
        String name

) {
    public static CategoryDtoRequest of(String code, String name, BoardGroup BoardGroup) {
        return CategoryDtoRequest.of(BoardGroup, code, name);
    }

    public static CategoryDtoRequest of(BoardGroup BoardGroup, String code, String name) {
        return new CategoryDtoRequest(BoardGroup, code, name);
    }

    public Category toEntity() {
        return Category.of(this.code, this.name, this.boardGroup.getId());
    }
}
