package com.aptner.v3.board.category.service;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.category.domain.Category;
import com.aptner.v3.board.category.dto.CreateCategoryDto;
import com.aptner.v3.board.category.dto.DeleteCategoryDto;
import com.aptner.v3.board.category.dto.UpdateCategoryDto;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.menu.domain.Menu;
import com.aptner.v3.board.menu.repository.MenuRepository;
import com.aptner.v3.global.exception.custom.AlreadyExistsCategoryNameException;
import com.aptner.v3.global.exception.custom.NotExistsCategoryIdException;
import com.aptner.v3.global.exception.custom.NotExistsMenuIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    public Category getCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(NotExistsCategoryIdException::new);
    }

    public void createCategory(long menuId, CreateCategoryDto.Request dto) {
        Menu menu = getMenu(menuId);

        checkNameIsExistsOrNot(dto.getCategoryName(), menu);

        categoryRepository.save(Category.of(dto.getCategoryName(), menu));
    }

    public void updateCategory(UpdateCategoryDto.Request dto) {
        categoryRepository.updateName(dto.getId(), dto.getCategoryName().getTableName());
    }

    public void deleteCategory(DeleteCategoryDto.Request dto) {
        categoryRepository.deleteById(dto.getId());
    }

    private Menu getMenu(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NotExistsMenuIdException::new);
    }

    private static void checkNameIsExistsOrNot(CategoryName name, Menu menu) {
        if (menu.getCategories()
                .stream().parallel()
                .anyMatch(caategory -> caategory.getCategoryName().equals(name)))
            throw new AlreadyExistsCategoryNameException();
    }
}
