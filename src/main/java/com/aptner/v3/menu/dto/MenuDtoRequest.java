package com.aptner.v3.menu.dto;

import com.aptner.v3.menu.Menu;

public record MenuDtoRequest(
        Long id,
        Long parentId,
        String code,
        String name
) {

    public static MenuDtoRequest of(String code, String name, Long parentId) {
        return MenuDtoRequest.of(null, parentId, code, name);
    }

    public static MenuDtoRequest of(Long id, Long parentId, String code, String name) {
        return new MenuDtoRequest(id, parentId, code, name);
    }

    public Menu toEntity() {
        return Menu.of(this.code, this.name, this.parentId);
    }
}
