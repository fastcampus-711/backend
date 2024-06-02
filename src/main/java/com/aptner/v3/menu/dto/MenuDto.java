package com.aptner.v3.menu.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.menu.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuDto {

    @Getter
    @AllArgsConstructor
    public static class MenuDtoRequest {

        Long parentId;
        @NotBlank
        String code;
        @NotBlank
        @Max(value = 50, message = "50자 이내로 입력해주세요.")
        String name;

        BoardGroup boardGroup;


        public static MenuDtoRequest of(String code, String name, Long parentId) {
            return MenuDtoRequest.of(parentId, code, name, null);
        }

        public static MenuDtoRequest of(Long parentId, String code, String name, BoardGroup boardGroup) {
            return new MenuDtoRequest(parentId, code, name, boardGroup);
        }

        public Menu toEntity() {
            return Menu.of(
                    code,
                    name,
                    boardGroup,
                    parentId
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MenuDtoResponse{
        Long id;
        Long parentId;
        String code;
        String name;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        BoardGroup boardGroup;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Set<MenuDtoResponse> list;

        public static MenuDtoResponse of(Long id, Long parentId, String code, String name, BoardGroup boardGroup) {

            Comparator<MenuDtoResponse> comparator = Comparator
                    .comparing(MenuDtoResponse::getId);

            return new MenuDtoResponse(id, parentId, code, name, boardGroup, new TreeSet<>(comparator));
        }

        public static MenuDtoResponse from(Menu menu) {

            return MenuDtoResponse.of(
                    menu.getId(),
                    menu.getParentId(),
                    menu.getCode(),
                    menu.getName(),
                    menu.getBoardGroup()
            );
        }

        public static Set<MenuDtoResponse> toList(List<Menu> menus) {
            Map<Long, MenuDtoResponse> map = menus.stream()
                    .map(MenuDtoResponse::from)
                    .collect(Collectors.toMap(MenuDtoResponse::getId, Function.identity())); // @todo

            // 부모가 있는 경우(하위 메뉴) (필터링)
            map.values().stream()
                    .filter(MenuDtoResponse::hasParent)
                    .forEach(child -> {
                        MenuDtoResponse parent = map.get(child.parentId);
                        parent.getList().add(child);
                    });

            // 부모가 없는 경우(상위 메뉴) (필터링)
            return map.values().stream()
                    .filter(child -> !child.hasParent())
                    .collect(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(MenuDtoResponse::getId))
                    ));
        }

        public boolean hasParent() {
            return this.parentId != null;
        }
    }
}
