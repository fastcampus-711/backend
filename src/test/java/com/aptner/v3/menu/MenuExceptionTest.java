package com.aptner.v3.menu;


import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MenuExceptionTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    @BeforeEach
    void setUp() {
        Menu unique = MenuDtoRequest.of("unique Key", "key", null).toEntity();
        menuRepository.save(unique);
    }

    @Test
    public void WhenUniqueKey_ThenThrowException() {
        // when
        Menu unique = MenuDtoRequest.of("unique Key", "key", null).toEntity();

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            menuRepository.save(unique);
        });
    }

    @Test
    public void WhenUniqueKey_ThenThrowMenuException() {
        // when
        MenuDtoRequest uniqueRequest = MenuDtoRequest.of("unique Key", "key", null);

        // then
        MenuException menuException = Assertions.assertThrows(MenuException.class, () -> {
            menuService.createMenu(uniqueRequest);
        });
        assertThat(menuException.getErrorCode()).isEqualTo(ErrorCode._ALREADY_EXIST);
    }
}
