package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDtoRequest;
import com.aptner.v3.board.category.dto.CategoryDtoResponse;
import com.aptner.v3.board.category.dto.CategorySearchRequest;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "분류")
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "분류 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseUtil.delete(
                CategoryDtoResponse.from(categoryService.deleteCategory(categoryId))
        );
    }

    @PostMapping("")
    @Operation(summary = "분류 생성")
    public ApiResponse<?> createCategory(@RequestBody CategoryDtoRequest categoryRequest) {
        return ResponseUtil.create(
                CategoryDtoResponse.from(categoryService.createCategory(categoryRequest))
        );
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "분류 수정")
    public ApiResponse<?> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryDtoRequest categoryRequest) {
        return ResponseUtil.update(
                CategoryDtoResponse.from(categoryService.updateCategory(categoryId, categoryRequest))
        );
    }

    @PostMapping("/search")
    @Operation(summary = "분류 조회")
    public ApiResponse<?> searchCategory(@RequestBody CategorySearchRequest searchRequest) {
        return ResponseUtil.ok(
                CategoryDtoResponse.toList(categoryService.search(searchRequest.menuId()))
        );
    }
}
