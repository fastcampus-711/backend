package com.aptner.v3.board.category.dto;

import com.aptner.v3.board.category.Category;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Category dto response.
 */
public record CategoryDtoResponse(
        Long id,
        Long menuId,
        String code,
        String name
) {

    /**
     * Of category dto response.
     *
     * @param id   the id
     * @param code the code
     * @param name the name
     * @return the category dto response
     */
    public static CategoryDtoResponse of(Long id, String code, String name) {
        return CategoryDtoResponse.of(id, code, name);
    }

    /**
     * Of category dto response.
     *
     * @param id     the id
     * @param menuId the menu id
     * @param code   the code
     * @param name   the name
     * @return the category dto response
     */
    public static CategoryDtoResponse of(Long id, Long menuId, String code, String name) {

        Comparator<CategoryDtoResponse> comparator = Comparator
                .comparing(CategoryDtoResponse::id);

        return new CategoryDtoResponse(id, menuId, code, name);
    }

    /**
     * From category dto response.
     *
     * @param category the category
     * @return the category dto response
     */
    public static CategoryDtoResponse from(Category category) {

        return CategoryDtoResponse.of(
                category.getId(),
                category.getMenuId(),
                category.getCode(),
                category.getName()
        );
    }

    /**
     * To list object.
     *
     * @param categories the categories
     * @return the object
     */
    public static Object toList(List<Category> categories) {
        // CategoryDtoResponse 변환
        Map<Long, CategoryDtoResponse> map = categories.stream()
                .map(CategoryDtoResponse::from)
                .collect(Collectors.toMap(CategoryDtoResponse::id, Function.identity()));

        // Grouping
        Map<Long, List<CategoryDtoResponse>> groupByMenuId = categories.stream()
                .collect(
                        Collectors.groupingBy(
                                Category::getMenuId,
                                Collectors.mapping(
                                        category -> map.get(category.getId()),
                                        Collectors.toList()
                                )
                        )
                );

        if(groupByMenuId.size() == 1) {
            return groupByMenuId.values().iterator().next(); // List<CategoryDtoResponse>
        }
        return groupByMenuId;                                // Map<Long, List<CategoryDtoResponse>>
    }

    /**
     * Has parent boolean.
     *
     * @return the boolean
     */
    public boolean hasMenu() {
        return this.menuId != null;
    }
}
