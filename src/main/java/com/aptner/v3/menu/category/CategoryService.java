package com.aptner.v3.menu.category;

import com.aptner.v3.menu.category.dto.CategoryDto;
import com.aptner.v3.menu.menu.Menu;
import com.aptner.v3.menu.menu.MenuRepository;
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

    public CategoryDto.Response deleteCategory(Long id) {

        // verify
        Category searched = getCategoryById(id);
        // set
        categoryRepository.deleteById(id);
        return modelMapper().map(searched, CategoryDto.Response.class);
    }

    public CategoryDto.Response createCategory(long menuId, CategoryDto.Request request) {

        // verify
        Menu verified = verifyCreate(menuId, request);
        // set
        Category category = modelMapper().map(request, Category.class);
        category.setMenu(verified);
        // save
        Category created = categoryRepository.save(category);
        return modelMapper().map(created, CategoryDto.Response.class);
    }

    public CategoryDto.Response updateCategory(long categoryId, CategoryDto.Request request) {
        // verify
        Category searched = getCategoryById(categoryId);
        searched.setName(request.getName());
        Category updated = categoryRepository.save(searched);
        return modelMapper().map(updated, CategoryDto.Response.class);
    }

    private Menu getMenuById(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(()-> new MenuException(_NOT_FOUND)); // NotExistsMenuIdException
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(_NOT_FOUND));
    }

    private Menu verifyCreate(long menuId, CategoryDto.Request request) {
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
