package com.aptner.v3.menu;


import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MenuExceptionTest {

    public static final String UNIQUE_KEY = "unique Key";

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    @Test
    public void WhenUniqueKey_ThenThrowMenuException() {
        // when
        Menu before = MenuDtoRequest.of(UNIQUE_KEY, "random", null).toEntity();
        menuRepository.save(before);
        menuRepository.flush();

        // given
        MenuDtoRequest uniqueRequest = MenuDtoRequest.of(UNIQUE_KEY, "random", null);

        // then
        MenuException menuException = Assertions.assertThrows(MenuException.class, () -> {
            menuService.createMenu(uniqueRequest);
        });
        assertThat(menuException.getErrorCode()).isEqualTo(ErrorCode._ALREADY_EXIST);
    }
}
