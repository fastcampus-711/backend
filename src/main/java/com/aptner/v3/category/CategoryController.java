package com.aptner.v3.category;

import com.aptner.v3.category.dto.CategoryDtoRequest;
import com.aptner.v3.category.dto.CategoryDtoResponse;
import com.aptner.v3.category.dto.CategorySearchRequest;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "카테고리")
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("id") long id) {
        return ResponseUtil.delete(categoryService.deleteCategory(id));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정")
    public ApiResponse<?> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryDtoRequest categoryRequest) {
        return ResponseUtil.update(
                CategoryDtoResponse.from(categoryService.updateCategory(categoryId, categoryRequest))
        );
    }

    @PostMapping("/search")
    @Operation(summary = "카테고리 조회")
    public ApiResponse<?> searchCategory(@RequestBody @Valid CategorySearchRequest searchRequest) {
        return ResponseUtil.ok(
                CategoryDtoResponse.toList(categoryService.search(searchRequest.boardGroup()))
        );
    }
}