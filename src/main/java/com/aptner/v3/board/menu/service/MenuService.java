package com.aptner.v3.board.menu.service;

import com.aptner.v3.board.menu.domain.Menu;
import com.aptner.v3.board.category.domain.Category;
import com.aptner.v3.board.menu.dto.CreateMenuDto;
import com.aptner.v3.board.menu.dto.DeleteMenuDto;
import com.aptner.v3.board.menu.dto.UpdateMenuDto;
import com.aptner.v3.board.menu.repository.MenuRepository;
import com.aptner.v3.global.exception.custom.NotExistsMenuIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> getMenuList() {
        return menuRepository.findAll();
    }

    public List<Category> getCategoryList(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NotExistsMenuIdException::new)
                .getCategories();
    }
}
