package com.aptner.v3.menu;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import com.aptner.v3.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;

@DisplayName("비즈니스 로직 - 메뉴")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService sut;

    @Mock
    private MenuRepository menuRepository;
    @DisplayName("메뉴 중복 생성. Menu Already Exists. 에러 발생")
    @Test
    void givenMenu_whenAddDuplicateKey_thenThrowException() {
        // Given
        MenuDtoRequest duplicateRequest = MenuDtoRequest.of("uniqueCode", "uniqueCodeName", null);

        // When
        when(menuRepository.save(any(Menu.class)))
                .thenThrow(new MenuException(ErrorCode._ALREADY_EXIST));

        // Then
        assertThrows(MenuException.class, () -> {
            sut.createMenu(duplicateRequest);
        });
    }
}