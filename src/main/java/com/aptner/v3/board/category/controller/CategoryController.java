package com.aptner.v3.board.category.controller;

import com.aptner.v3.board.category.domain.Category;
import com.aptner.v3.board.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
