package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDtoRequest;
import com.aptner.v3.board.category.dto.CategorySearchRequest;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "분류")
@RequestMapping("/")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/categories/{category_id}")
    @Operation(summary = "분류 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("category_id") Long categoryId) {
        return ResponseUtil.delete(categoryService.deleteCategory(categoryId));
    }

    @PostMapping("/categories")
    @Operation(summary = "분류 생성")
    public ApiResponse<?> createCategory(@RequestBody CategoryDtoRequest categoryRequest) {
        return ResponseUtil.create(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/categories/{category_id}")
    @Operation(summary = "분류 수정")
    public ApiResponse<?> updateCategory(@PathVariable("category_id") Long categoryId, @RequestBody CategoryDtoRequest categoryRequest) {
        return ResponseUtil.update(categoryService.updateCategory(categoryId, categoryRequest));
    }

    @GetMapping("/categories")
    @Operation(summary = "분류 조회")
    public ApiResponse<?> searchCategory(@RequestBody CategorySearchRequest searchRequest) {
        return ResponseUtil.ok(categoryService.search(searchRequest.menuId()));
    }
}
