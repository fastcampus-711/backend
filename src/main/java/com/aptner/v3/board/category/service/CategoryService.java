package com.aptner.v3.board.category.service;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.category.domain.Category;
import com.aptner.v3.board.category.dto.CreateCategoryDto;
import com.aptner.v3.board.category.dto.DeleteCategoryDto;
import com.aptner.v3.board.category.dto.UpdateCategoryDto;
import com.aptner.v3.board.category.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public Category getCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(NotExistsCategoryIdException::new);
    }
}
