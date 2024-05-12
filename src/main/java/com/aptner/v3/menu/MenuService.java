package com.aptner.v3.menu;

import com.aptner.v3.global.exception.MenuException;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import com.aptner.v3.menu.dto.MenuDtoResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> getMenuList() {
        return menuRepository.findAllActiveWithActiveParent();
    }

    public MenuDtoResponse createMenu(MenuDtoRequest request) {
        // verify
        verifyCreate(request);

        try {
            Menu created = menuRepository.save(request.toEntity());
            return MenuDtoResponse.from(created);
        } catch(DataIntegrityViolationException e) {
            throw new MenuException(_ALREADY_EXIST);
        }
    }

    public MenuDtoResponse deleteMenu(Long id) {
        Menu menu = getMenuById(id);

        menuRepository.deleteById(id);
        return MenuDtoResponse.from(menu);
    }

    public MenuDtoResponse updateMenu(Long id, MenuDtoRequest request) {
        Menu menu = getMenuById(id);

        verifyUpdate(request, menu);
        menuRepository.flush();
        return MenuDtoResponse.from(menu);
    }

    private static void verifyUpdate(MenuDtoRequest request, Menu menu) {
        // update
        if(StringUtils.isNotEmpty(request.code())) {
            menu.setCode(request.code());}
        if(StringUtils.isNotEmpty(request.name())) {
            menu.setName(request.name());}
    }

    private void verifyCreate(MenuDtoRequest request) {

        // check parent Menu
        if (request.parentId() != null) {
            if (!checkParentExists(request.parentId())) {
                throw new MenuException(_NOT_FOUND);
            }
        }
    }

    private boolean checkParentExists(long menuId) {
        return menuRepository.existsById(menuId);
    }

    private Menu getMenuById(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(_NOT_FOUND));
    }

    public List<Menu> getSubMenuById(long menuId) {
        return menuRepository.findByIdOrParentId(menuId, menuId);
    }

}
