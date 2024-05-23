package com.aptner.v3.menu.dto;

import com.aptner.v3.category.BoardGroup;
import com.aptner.v3.menu.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Menu dto response.
 */
public record MenuDtoResponse(
        Long id,
        Long parentId,
        String code,
        String name,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        BoardGroup boardGroup,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Set<MenuDtoResponse> list
) {
    /**
     * Of menu dto response.
     *
     * @param id   the id
     * @param code the code
     * @param name the name
     * @return the menu dto response
     */
    public static MenuDtoResponse of(Long id, String code, String name, BoardGroup boardGroup) {
        return MenuDtoResponse.of(id, code, name, boardGroup);
    }

    /**
     * Of menu dto response.
     *
     * @param id       the id
     * @param parentId the parent id
     * @param code     the code
     * @param name     the name
     * @return the menu dto response
     */
    public static MenuDtoResponse of(Long id, Long parentId, String code, String name, BoardGroup boardGroup) {

        Comparator<MenuDtoResponse> comparator = Comparator
                .comparing(MenuDtoResponse::id);

        return new MenuDtoResponse(id, parentId, code, name, boardGroup, new TreeSet<>(comparator));
    }

    /**
     * From menu dto response.
     *
     * @param menu the menu
     * @return the menu dto response
     */
    public static MenuDtoResponse from(Menu menu) {

        return MenuDtoResponse.of(
                menu.getId(),
                menu.getParentId(),
                menu.getCode(),
                menu.getName(),
                menu.getBoardGroup()
        );
    }

    /**
     * To list set.
     *
     * @param menus the menus
     * @return the set
     */
    public static Set<MenuDtoResponse> toList(List<Menu> menus) {
        Map<Long, MenuDtoResponse> map = menus.stream()
                .map(MenuDtoResponse::from)
                .collect(Collectors.toMap(MenuDtoResponse::id, Function.identity())); // @todo

        // 부모가 있는 경우(하위 메뉴) (필터링)
        map.values().stream()
                .filter(MenuDtoResponse::hasParent)
                .forEach(child -> {
                    MenuDtoResponse parent = map.get(child.parentId);
                    parent.list().add(child);
                });

        // 부모가 없는 경우(상위 메뉴) (필터링)
        return map.values().stream()
                .filter(child -> !child.hasParent())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(MenuDtoResponse::id))
                ));
    }

    /**
     * Has parent boolean.
     *
     * @return the boolean
     */
    public boolean hasParent() {
        return this.parentId != null;
    }
}
