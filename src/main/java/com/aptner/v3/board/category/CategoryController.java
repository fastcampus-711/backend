package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.global.error.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판")
@RequestMapping("/")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "게시판 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("id") long id) {
        return ResponseUtil.delete(categoryService.deleteCategory(id));
    }

    @PostMapping("/categories")
    @Operation(summary = "게시판 생성")
    public ApiResponse<?> createCategory(@PathVariable("menu_id") long menuId, @RequestBody CategoryDto.Request request) {
        return ResponseUtil.create(categoryService.createCategory(menuId, request));
    }

    @PutMapping("/categories/{category_id}")
    @Operation(summary = "게시판 수정")
    public ApiResponse<?> updateCategory(@PathVariable("category_id") long categoryId, @RequestBody CategoryDto.Request request) {
        return ResponseUtil.update(categoryService.updateCategory(categoryId, request));
    }
}
