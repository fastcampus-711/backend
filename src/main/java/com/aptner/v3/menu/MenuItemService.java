package com.aptner.v3.menu;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    static int PARENT_ROOT = 0;

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAdminList() {
        return menuItemRepository.findByPageRole(1);
    }

    public List<MenuItem> getFrontList() {
        return menuItemRepository.findByPageRole(2);
    }

    public List<MenuItem> getParents(List<MenuItem> menuList) {
        return menuList.stream()
                .filter(item -> item.getParent() == null)
                .collect(Collectors.toList());
    }

    public Map<MenuItem, List<MenuItem>> getchilds(List<MenuItem> menuList) {
        return menuList.stream()
                .filter(item -> item.getParent() != null)
                .collect(Collectors.groupingBy(MenuItem::getParent));
    }

}