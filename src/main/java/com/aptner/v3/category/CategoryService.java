package com.aptner.v3.category;

import com.aptner.v3.category.dto.CategoryDtoRequest;
import com.aptner.v3.category.repository.CategoryRepository;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.MenuService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;


@Slf4j
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

    public Category createCategory(CategoryDtoRequest request) {

        // verify
        verifyCreate(request);

        try {
            return categoryRepository.save(request.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new CategoryException(_ALREADY_EXIST);
        }
    }

    public Category deleteCategory(Long id) {
        Category category = getCategoryById(id);

        categoryRepository.deleteById(id);
        return category;
    }

    public Category updateCategory(long categoryId, CategoryDtoRequest request) {
        Category category = getCategoryById(categoryId);

        // verify
        verifyUpdate(request, category);
        categoryRepository.flush();

        return category;
    }

    private void verifyUpdate(CategoryDtoRequest request, Category category) {
        // code
        if (StringUtils.isNotEmpty(request.code())) {
            checkDuplicatedCode(request);
            category.setCode(request.code());
        }
        // name
        if (StringUtils.isNotEmpty(request.name())) {
            category.setName(request.name());
        }
    }

    private void verifyCreate(CategoryDtoRequest request) {
        // menuId
        if (request.menuId() != null) {
            if (!menuService.isExistsMenu(request.menuId())) {
                throw new MenuException(_NOT_FOUND);
            }
        }
        // code
        if (StringUtils.isNotEmpty(request.code())) {
            checkDuplicatedCode(request);
        }
    }

    private void checkDuplicatedCode(CategoryDtoRequest request) {
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
