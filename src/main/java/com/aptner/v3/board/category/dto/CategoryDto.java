package com.aptner.v3.board.category.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CategoryDto {
    Long id;
    String code;
    String name;
    BoardGroup boardGroup;

    public CategoryDto(Long id, String code, String name, BoardGroup boardGroup) {
        this.id = id;
        this.boardGroup = boardGroup;
        this.code = code;
        this.name = name;
    }

    public static CategoryDto of(Long id) {
        return new CategoryDto(id, null, null, null);
    }

    public static CategoryDto from(Category entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                BoardGroup.getById(entity.getBoardGroup())
        );
    }

    @Getter
    @AllArgsConstructor
    public static class CategoryRequest {
        BoardGroup boardGroup;
        @NotBlank
        String code;
        String name;


        public static CategoryRequest of(String code, String name, BoardGroup BoardGroup) {
            return CategoryRequest.of(BoardGroup, code, name);
        }

        public static CategoryRequest of(BoardGroup BoardGroup, String code, String name) {
            return new CategoryRequest(BoardGroup, code, name);
        }

        public Category toEntity() {
            return Category.of(
                    this.code,
                    this.name,
                    this.boardGroup.getId()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CategoryResponse {

        Long id;
        BoardGroup boardGroup;
        String code;
        String name;

        public static CategoryResponse of(Long id, BoardGroup boardGroup, String code, String name) {

            return new CategoryResponse(id, boardGroup, code, name);
        }


        public static CategoryResponse from(Category category) {

            return CategoryResponse.of(
                    category.getId(),
                    BoardGroup.getById(category.getBoardGroup()),
                    category.getCode(),
                    category.getName()
            );
        }

        public static Object toList(List<Category> categories) {
            // CategoryDtoResponse 변환
            Map<Long, CategoryResponse> map = categories.stream()
                    .map(CategoryResponse::from)
                    .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));

            // Grouping
            Map<Long, List<CategoryResponse>> groupByMenuId = categories.stream()
                    .collect(
                            Collectors.groupingBy(
                                    Category::getBoardGroup,
                                    Collectors.mapping(
                                            category -> map.get(category.getId()),
                                            Collectors.toList()
                                    )
                            )
                    );

            if (groupByMenuId.size() == 1) {
                return groupByMenuId.values().iterator().next(); // List<CategoryDtoResponse>
            }
            return groupByMenuId;                                // Map<Long, List<CategoryDtoResponse>>
        }
    }
}
