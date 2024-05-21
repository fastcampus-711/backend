package com.aptner.v3.menu;

import com.aptner.v3.global.config.JpaConfig;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import com.aptner.v3.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase
public class MenuJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("생성 테스트")
    void givenData_whenInsert_thenCreated() {
        // Given
        long previousCount = menuRepository.count();
        Menu parent = MenuDtoRequest.of("parent", "parent", null).toEntity();
        Menu child = MenuDtoRequest.of("child", "child", parent.getId()).toEntity();

        menuRepository.save(parent);
        menuRepository.save(child);

        // Then
        assertThat(menuRepository.count()).isEqualTo(previousCount + 2);
    }

    @Test
    @DisplayName("생성 테스트 - 에러 발생")
    void givenData_whenInsertDuplicatedKey_thenThrowException() {
        // Given
        Menu parent = MenuDtoRequest.of("parent", "parent", null).toEntity();

        Menu saved = menuRepository.save(parent);
        Menu duplicated = menuRepository.save(parent);
        assertThat(saved).isEqualTo(duplicated);
    }

    @Test
    @DisplayName("하위 메뉴 삭제 테스트")
    void givenData_whenSubMenuDelete_thenDeleted() {
        // Given
        long previousCount = menuRepository.count();
        Menu parent = MenuDtoRequest.of("parent", "parent", null).toEntity();
        Menu child = MenuDtoRequest.of("child", "child", parent.getId()).toEntity();
        menuRepository.save(parent);
        menuRepository.save(child);

        // delete
        menuRepository.delete(child);

        // Then
        assertThat(menuRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("상위 메뉴 삭제 테스트")
    void givenData_whenTopMenuDelete_thenDeleted() {
        // Given
        long previousCount = menuRepository.count();
        Menu parent = MenuDtoRequest.of("parent", "parent", null).toEntity();
        Menu child = MenuDtoRequest.of("child", "child", parent.getId()).toEntity();
        menuRepository.save(parent);
        menuRepository.save(child);

        // delete
        menuRepository.delete(parent);

        // Then
        assertThat(menuRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("하위 메뉴 조회")
    void givenId_whenSearch_thenReturnContainParentId() {

        // Setup data scenario
        Menu menu1 = Menu.of("code1", "code1", null, null);
        entityManager.persist(menu1);
        Menu menu2 = Menu.of("code2", "code2", null, menu1.getId());
        entityManager.persist(menu2);
        Menu menu3 = Menu.of("code3", "code3", null, menu2.getId());
        entityManager.persist(menu3);
        entityManager.flush();

        // Execute the query
        List<Menu> results = menuRepository.findByIdOrParentId(menu1.getId(), menu1.getId());

        // Verify the results
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name", String.class).containsExactlyInAnyOrder("code1", "code2");
    }

}
