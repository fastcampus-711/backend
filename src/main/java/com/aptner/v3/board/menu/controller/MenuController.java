package com.aptner.v3.board.menu.controller;

import com.aptner.v3.board.menu.dto.CreateMenuDto;
import com.aptner.v3.board.menu.dto.DeleteMenuDto;
import com.aptner.v3.board.menu.dto.UpdateMenuDto;
import com.aptner.v3.board.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<?> getMenuList() {
        return new ResponseEntity<>(menuService.getMenuList(), HttpStatus.OK);
    }

    @GetMapping("/{menu-id}")
    public ResponseEntity<?> getCategoryList
            (@PathVariable("menu-id") long menuId) {
        return new ResponseEntity<>(menuService.getCategoryList(menuId), HttpStatus.OK);
    }

    @PostMapping
    @Deprecated
    public ResponseEntity<?> createMenu(@RequestBody CreateMenuDto.Request dto) {
        menuService.createMenu(dto);
        return new ResponseEntity<>("add " + dto + " to menu success", HttpStatus.OK);
    }

    @PutMapping
    @Deprecated
    public ResponseEntity<?> updateMenu(@RequestBody UpdateMenuDto.Request dto) {
        menuService.updateMenu(dto);
        return new ResponseEntity<>("update " + dto.getTargetId() + " to " + dto.getTo() + " success", HttpStatus.OK);
    }

    @DeleteMapping
    @Deprecated
    public ResponseEntity<?> deleteMenu(@RequestBody DeleteMenuDto.Request dto) {
        menuService.deleteMenu(dto);
        return new ResponseEntity<>("delete " + dto.getId() + " from menu success.", HttpStatus.OK);
    }
}