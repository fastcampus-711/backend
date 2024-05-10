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

    public MenuDto.MenuResponse createMenu(MenuDto.MenuRequest menuRequest) {
        // @todo mapStruct
        Menu converted = Menu.of(MenuName.valueOf(menuRequest.getCode()), menuRequest.getName());

        Menu created = menuRepository.save(converted);
        return modelMapper().map(created, MenuDto.MenuResponse.class);
    }

    public MenuDto.MenuResponse deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuException(_NOT_FOUND));

        menuRepository.deleteById(id);
        return modelMapper().map(menu, MenuDto.MenuResponse.class);
    }

    public MenuDto.MenuResponse updateMenu(Long id, MenuDto.MenuRequest menuRequest) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuException(_NOT_FOUND));

        // update
        menu.setCode(MenuName.valueOf(menuRequest.getCode()));
        menu.setName(menuRequest.getName());

        Menu updated = menuRepository.save(menu);
        return modelMapper().map(updated, MenuDto.MenuResponse.class);
    }

    public List<Category> getCategoryList(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(_NOT_FOUND))
                .getCategories();
    }

}
