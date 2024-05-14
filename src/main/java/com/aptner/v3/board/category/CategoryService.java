package com.aptner.v3.board.category;

import com.aptner.v3.board.category.dto.categoryDto;
import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.MenuRepository;
import com.aptner.v3.global.exception.CategoryException;
import com.aptner.v3.global.exception.MenuException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.aptner.v3.CommunityApplication.modelMapper;
import static com.aptner.v3.global.error.ErrorCode._ALREADY_EXIST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

@Service
@Transactional
public class CategoryService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(
            MenuRepository menuRepository,
            CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    public categoryDto.Response deleteCategory(Long id) {

        // verify
        Category searched = getCategoryById(id);
        // set
        categoryRepository.deleteById(id);
        return modelMapper().map(searched, categoryDto.Response.class);
    }

    public categoryDto.Response createCategory(long menuId, categoryDto.Request request) {

        // verify
        Menu verified = verifyCreate(menuId, request);
        // set
        Category category = modelMapper().map(request, Category.class);
        category.setMenu(verified);
        // save
        Category created = categoryRepository.save(category);
        return modelMapper().map(created, categoryDto.Response.class);
    }

    public categoryDto.Response updateCategory(long categoryId, categoryDto.Request request) {
        // verify
        Category searched = getCategoryById(categoryId);
        searched.setName(request.getName());
        Category updated = categoryRepository.save(searched);
        return modelMapper().map(updated, categoryDto.Response.class);
    }

    private Menu getMenuById(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(()-> new MenuException(_NOT_FOUND)); // NotExistsMenuIdException
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(_NOT_FOUND));
    }

    private Menu verifyCreate(long menuId, categoryDto.Request request) {
        // check menu
        Menu menu = getMenuById(menuId);
        // check category
        categoryRepository.findByMenuIdAndName(menuId, request.getName())
                .ifPresent(c -> {
                    throw new CategoryException(_ALREADY_EXIST);
                });
        return menu;
    }

}
