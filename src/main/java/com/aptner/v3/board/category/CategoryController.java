package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.category.dto.CategorySearchRequest;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "분류")
@RequestMapping("/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/{category_id}")
    @Operation(summary = "분류 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("category_id") long categoryId) {
        return ResponseUtil.delete(categoryService.deleteCategory(categoryId));
    }

    @PutMapping("/{category_id}")
    @Operation(summary = "분류 수정")
    public ApiResponse<?> updateCategory(@PathVariable("category_id") Long categoryId, @RequestBody CategoryDto.CategoryRequest categoryRequest) {
        return ResponseUtil.update(
                CategoryDto.CategoryResponse.from(categoryService.updateCategory(categoryId, categoryRequest))
        );
    }

    @PostMapping("/search")
    @Operation(summary = "분류 조회")
    public ApiResponse<?> searchCategory(@RequestBody @Valid CategorySearchRequest searchRequest) {
        return ResponseUtil.ok(
                CategoryDto.CategoryResponse.toList(categoryService.search(searchRequest.boardGroup()))
        );
    }
}