package com.aptner.v3.category;

import com.aptner.v3.category.Category;
import com.aptner.v3.category.CategoryService;
import com.aptner.v3.category.dto.CategoryDtoRequest;
import com.aptner.v3.category.repository.CategoryRepository;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 로직 - 분류")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService sut;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MenuService menuService;

    @Test
    @DisplayName("새로운 카테고리 생성 - 메뉴 존재")
    void whenCreateCategory_success() {
        // Given
        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", 1L);
        Category expectedCategory = Category.of("code", "value", 1L);

        given(menuService.isExistsMenu(1L)).willReturn(true); // Exists.
        given(categoryRepository.save(any(Category.class))).willReturn(expectedCategory);

        // When
        Category result = sut.createCategory(request);

        // Then
        assertNotNull(result);
        assertEquals(expectedCategory.getId(), result.getId());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("새로운 카테고리 생성 - 메뉴 존재 안함")
    void whenCreateCategory_thenThrowException() {
        // Given
        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", 1L);
        // when
        when(menuService.isExistsMenu(1L))
                .thenThrow(new MenuException(ErrorCode._NOT_FOUND)); // Not Exists.
        // Then
        assertThrows(MenuException.class, () -> {
            sut.createCategory(request);
        });
    }

    @Test
    @DisplayName("카테고리 생성 실패 - 중복 데이터")
    void createCategory_throwsExceptionWhenDuplicateData() {
        // 준비
        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", 1L);
        given(menuService.isExistsMenu(1L)).willReturn(true); // Exists.
        when(categoryRepository.save(any(Category.class)))
                .thenThrow(new DataIntegrityViolationException("Already exists"));

        // 실행 & 검증
        assertThrows(CategoryException.class, () -> sut.createCategory(request));
        verify(categoryRepository).save(any(Category.class));
    }
}