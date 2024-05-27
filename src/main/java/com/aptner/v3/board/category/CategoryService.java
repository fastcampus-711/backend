package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.menu.MenuService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aptner.v3.global.error.ErrorCode.*;


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

    public List<Category> search(BoardGroup boardGroup) {

        log.debug("boardGroup: {},{}", boardGroup, boardGroup.getDomain());
        if (BoardGroup.ALL.equals(boardGroup)) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findByBoardGroup(boardGroup.getId());
    }

    public Category createCategory(CategoryDto.CategoryRequest request) {

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

    public Category updateCategory(long categoryId, CategoryDto.CategoryRequest request) {
        Category category = getCategoryById(categoryId);

        // verify
        verifyUpdate(request, category);
        categoryRepository.flush();

        return category;
    }

    private void verifyUpdate(CategoryDto.CategoryRequest request, Category category) {
        // code
        if (StringUtils.isNotEmpty(request.getCode())) {
            checkDuplicatedCode(request);
            category.setCode(request.getCode());
        }
        // name
        if (StringUtils.isNotEmpty(request.getName())) {
            category.setName(request.getName());
        }
        // menu check
        if (!menuService.isExistsMenu(request.getBoardGroup().getMenuId())) {
            throw new CategoryException(NOT_EXISTS_MENU_ID_EXCEPTION);
        }
    }

    private void verifyCreate(CategoryDto.CategoryRequest request) {
        // code
        if (StringUtils.isNotEmpty(request.getCode())) {
            checkDuplicatedCode(request);
        }
        // menu check
        if (!menuService.isExistsMenu(request.getBoardGroup().getMenuId())) {
            throw new CategoryException(NOT_EXISTS_MENU_ID_EXCEPTION);
        }
    }

    private void checkDuplicatedCode(CategoryDto.CategoryRequest request) {
        // duplicate code (constraint)
        categoryRepository.findByCode(request.getCode())
                .ifPresent(c -> {
                    throw new CategoryException(_ALREADY_EXIST);
                });
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(_NOT_FOUND));
    }

}