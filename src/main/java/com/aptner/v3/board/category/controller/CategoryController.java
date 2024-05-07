package com.aptner.v3.board.category.controller;

import com.aptner.v3.board.category.domain.Category;
import com.aptner.v3.board.category.dto.CreateCategoryDto;
import com.aptner.v3.board.category.dto.DeleteCategoryDto;
import com.aptner.v3.board.category.dto.UpdateCategoryDto;
import com.aptner.v3.board.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/{menu-id}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{category-id}")
    public String getPostList(@PathVariable("menu-id") long menuId, @PathVariable("category-id") long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        return "redirect:/menus/" + menuId + "/categories/" + categoryId + "/" + category.getCategoryName().getURI();
    }

    @PostMapping
    @Deprecated
    public ResponseEntity<?> createCategory(@PathVariable("menu-id") long menuId, @RequestBody CreateCategoryDto.Request dto) {
        categoryService.createCategory(menuId, dto);
        return new ResponseEntity<>("add " + dto + " to category success", HttpStatus.OK);
    }

    @PutMapping
    @Deprecated
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryDto.Request dto) {
        categoryService.updateCategory(dto);
        return new ResponseEntity<>("update " + dto.getId() + " to " + dto.getCategoryName() + " success", HttpStatus.OK);
    }

    @DeleteMapping
    @Deprecated
    public ResponseEntity<?> deleteCategory(@RequestBody DeleteCategoryDto.Request dto) {
        categoryService.deleteCategory(dto);
        return new ResponseEntity<>("delete " + dto.getId() + " from category success)", HttpStatus.OK);
    }
}
