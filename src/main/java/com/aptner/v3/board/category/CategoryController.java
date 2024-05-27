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

@Tag(name = "게시판")
@RequestMapping("/")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "게시판 삭제")
    public ApiResponse<?> deleteCategory(@PathVariable("id") long id) {
        return ResponseUtil.delete(categoryService.deleteCategory(id));
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
