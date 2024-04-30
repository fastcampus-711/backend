package com.aptner.v3.menu.controller;

import com.aptner.v3.menu.MenuItem;
import com.aptner.v3.menu.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<MenuItem>> getAdminMenu() {
        return ResponseEntity.ok(menuItemService.getParents(menuItemService.getAdminList()));
    }

    @GetMapping("/front")
    public ResponseEntity<List<MenuItem>> getFrontMenu() {
        return ResponseEntity.ok(menuItemService.getParents(menuItemService.getFrontList()));
    }

}
