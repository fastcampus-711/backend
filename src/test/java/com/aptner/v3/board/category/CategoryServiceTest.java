package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
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
        CategoryDto.CategoryRequest request = CategoryDto.CategoryRequest.of("code", "value", BoardGroup.FREES);
        Category expectedCategory = Category.of("code", "value", BoardGroup.FREES.getId());

        given(menuService.isExistsMenu(BoardGroup.FREES.getMenuId())).willReturn(true); // Exists.
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
    void whenCreateCategory_thenMenuNotExists() {
        // Given
        CategoryDto.CategoryRequest request = CategoryDto.CategoryRequest.of("code", "value", BoardGroup.MARKETS);
        // when
        when(menuService.isExistsMenu(BoardGroup.MARKETS.getMenuId()))
                .thenThrow(new CategoryException(ErrorCode.NOT_EXISTS_MENU_ID_EXCEPTION)); // Not Exists.
        // Then
        assertThrows(CategoryException.class, () -> sut.createCategory(request));
    }

    @Test
    @DisplayName("카테고리 생성 실패 - 중복 데이터")
    void whenCreateCategory_thenThrowException() {
        // Given
        CategoryDto.CategoryRequest request = CategoryDto.CategoryRequest.of("code", "value", BoardGroup.FREES);
        // when
        when(categoryRepository.findByCode(any(String.class)))
                .thenThrow(new CategoryException(_ALREADY_EXIST));
        // Then
        assertThrows(CategoryException.class, () -> sut.createCategory(request));
    }
}