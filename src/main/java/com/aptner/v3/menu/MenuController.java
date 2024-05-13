package com.aptner.v3.menu;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import com.aptner.v3.menu.dto.MenuDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="상위 메뉴")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "상위 메뉴 조회")
    public ApiResponse<?> getMenu() {
        return ResponseUtil.ok(
                MenuDtoResponse.list(menuService.getMenuList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "하위 메뉴 조회")
    public ApiResponse<?> getSubMenu(@PathVariable("id") long menuId) {
        return ResponseUtil.ok(
                MenuDtoResponse.list(menuService.getSubMenuById(menuId))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "메뉴 삭제")
    public ApiResponse<?> deleteMenu(@PathVariable Long id) {
        return ResponseUtil.delete(menuService.deleteMenu(id));
    }

    @PostMapping
    @Operation(summary = "메뉴 생성")
    public ApiResponse<?> createMenu(@RequestBody MenuDtoRequest request) {
        return ResponseUtil.create(menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "메뉴 수정")
    public ApiResponse<?> updateMenu(@PathVariable Long id, @RequestBody MenuDtoRequest request) {
       return ResponseUtil.update(menuService.updateMenu(id, request));
    }

}
