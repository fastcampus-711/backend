package com.aptner.v3.menu.menu;

import com.aptner.v3.menu.category.Category;
import com.aptner.v3.menu.menu.dto.MenuDto;
import com.aptner.v3.global.exception.MenuException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aptner.v3.CommunityApplication.modelMapper;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> getMenuList() {
        return menuRepository.findAll();
    }

    public MenuDto.Response createMenu(MenuDto.Request request) {
        // @todo mapStruct
        Menu converted = new Menu(MenuName.valueOf(request.getCode()),request.getName());

        Menu created = menuRepository.save(converted);
        return modelMapper().map(created, MenuDto.Response.class);
    }

    public MenuDto.Response deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuException(_NOT_FOUND));

        menuRepository.deleteById(id);
        return modelMapper().map(menu, MenuDto.Response.class);
    }

    public MenuDto.Response updateMenu(Long id, MenuDto.Request request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuException(_NOT_FOUND));

        // update
        menu.setCode(MenuName.valueOf(request.getCode()));
        menu.setName(request.getName());

        Menu updated = menuRepository.save(menu);
        return modelMapper().map(updated, MenuDto.Response.class);
    }

    public List<Category> getCategoryList(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(_NOT_FOUND))
                .getCategories();
    }

}
