package com.aptner.v3.menu.dto;

import com.aptner.v3.menu.Menu;
import jakarta.validation.constraints.NotBlank;

public record MenuDtoRequest(
        Long parentId,
        @NotBlank
        String code,
        String name,
        String url
) {

    public static MenuDtoRequest of(String code, String name, Long parentId) {
        return MenuDtoRequest.of(parentId, code, name, null);
    }

    public static MenuDtoRequest of(Long parentId, String code, String name, String url) {
        return new MenuDtoRequest(parentId, code, name, url);
    }

    public Menu toEntity() {
        return Menu.of(this.code, this.name, this.url, this.parentId);
    }
}
