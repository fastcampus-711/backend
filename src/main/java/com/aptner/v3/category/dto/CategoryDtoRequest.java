package com.aptner.v3.category.dto;

import com.aptner.v3.category.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDtoRequest(
        Long menuId,
        @NotBlank
        String code,
        String name

) {
    public static CategoryDtoRequest of(String code, String name, Long menuId) {
        return CategoryDtoRequest.of(menuId, code, name);
    }

    public static CategoryDtoRequest of(Long menuId, String code, String name) {
        return new CategoryDtoRequest(menuId, code, name);
    }

    public Category toEntity() {
        return Category.of(this.code, this.name, this.menuId);
    }
}
