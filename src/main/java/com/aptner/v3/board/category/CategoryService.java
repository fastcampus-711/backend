package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDtoRequest;
import com.aptner.v3.board.category.dto.CategoryDtoResponse;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.MenuService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuService menuService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, MenuService menuService) {
        this.categoryRepository = categoryRepository;
        this.menuService = menuService;
    }

    public List<Category> search(Long menuId) {
        if (menuId != null) {
            return categoryRepository.findByMenuId(menuId);
        }
        return categoryRepository.findAll();
    }

    public CategoryDtoResponse createCategory(CategoryDtoRequest request) {

        // verify
        verifyCreate(request);

        try {
            Category created = categoryRepository.save(request.toEntity());
            return CategoryDtoResponse.from(created);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryException(_ALREADY_EXIST);
        }
    }

    public CategoryDtoResponse deleteCategory(Long id) {
        Category category = getCategoryById(id);

        categoryRepository.deleteById(id);
        return CategoryDtoResponse.from(category);
    }

    public CategoryDtoResponse updateCategory(long categoryId, CategoryDtoRequest request) {
        Category category = getCategoryById(categoryId);

        verifyUpdate(request, category);
        categoryRepository.flush();
        return CategoryDtoResponse.from(category);
    }

    private static void verifyUpdate(CategoryDtoRequest request, Category category) {
        // update
        if (StringUtils.isNotEmpty(request.code())) {
            category.setCode(request.code());
        }
        if (StringUtils.isNotEmpty(request.name())) {
            category.setName(request.name());
        }
    }

    private void verifyCreate(CategoryDtoRequest request) {

        // check menu
        if (request.menuId() != null) {
            if (!menuService.isExistsMenu(request.menuId())) {
                throw new MenuException(_NOT_FOUND);
            }
        }

        // duplicate code (constraint)
        categoryRepository.findByCode(request.code())
                .ifPresent(c -> {
                    throw new CategoryException(_ALREADY_EXIST);
                });
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(_NOT_FOUND));
    }

}
