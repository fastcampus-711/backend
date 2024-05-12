package com.aptner.v3.menu.dto;

import com.aptner.v3.menu.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record MenuDtoResponse(
        Long id,
        Long parentId,
        String code,
        String name,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Set<MenuDtoResponse> sub
) {
    public static MenuDtoResponse of(Long id, String code, String name) {
        return MenuDtoResponse.of(id, code, name);
    }

    public static MenuDtoResponse of(Long id, Long parentId, String code, String name) {

        Comparator<MenuDtoResponse> comparator = Comparator
                .comparing(MenuDtoResponse::id);

        return new MenuDtoResponse(id, parentId, code, name, new TreeSet<>(comparator));
    }

    public static MenuDtoResponse from(Menu menu) {

        return MenuDtoResponse.of(
                menu.getId(),
                menu.getParentId(),
                menu.getCode(),
                menu.getName()
        );
    }

    public static Set<MenuDtoResponse> list(List<Menu> menus) {
        Map<Long, MenuDtoResponse> map = menus.stream()
                .map(MenuDtoResponse::from)
                .collect(Collectors.toMap(MenuDtoResponse::id, Function.identity())); // @todo

        // 부모가 있는 경우(하위 메뉴) (필터링)
        map.values().stream()
                .filter(MenuDtoResponse::hasParent)
                .forEach(child -> {
                    MenuDtoResponse parent = map.get(child.parentId());
                    parent.sub().add(child);
                });

        // 부모가 없는 경우(상위 메뉴) (필터링)
        return map.values().stream()
                .filter(child -> !child.hasParent())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(MenuDtoResponse::id))
                ));
    }

    public boolean hasParent() {
        return this.parentId != null;
    }
}
