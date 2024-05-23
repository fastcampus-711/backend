package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDtoRequest;
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

import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;


@Slf4j
@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, MenuService menuService) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> search(BoardGroup boardGroup) {

        log.debug("boardGroup: {},{}", boardGroup, boardGroup.getDomain());
        if (boardGroup.ALL == boardGroup) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findByBoardGroup(boardGroup.getId());
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