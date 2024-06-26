package com.aptner.v3.menu;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import com.aptner.v3.menu.dto.MenuDto;
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
    @Operation(summary = "전체 메뉴 조회")
    public ApiResponse<?> getMenu() {
        return ResponseUtil.ok(
                MenuDto.MenuDtoResponse.toList(menuService.getMenuList())
        );
    }

    @GetMapping("/{menuId}")
    @Operation(summary = "하위 메뉴 조회")
    public ApiResponse<?> getSubMenu(@PathVariable("menuId") long menuId) {
        return ResponseUtil.ok(
                MenuDto.MenuDtoResponse.toList(menuService.getSubMenuById(menuId))
        );
    }

    @DeleteMapping("/{menuId}")
    @Operation(summary = "메뉴 삭제")
    public ApiResponse<?> deleteMenu(@PathVariable("menuId") long id) {
        return ResponseUtil.delete(
                MenuDto.MenuDtoResponse.from(menuService.deleteMenu(id))
        );
    }

    @PostMapping
    @Operation(summary = "메뉴 생성")
    public ApiResponse<?> createMenu(@RequestBody MenuDto.MenuDtoRequest request) {
        return ResponseUtil.create(
                MenuDto.MenuDtoResponse.from(menuService.createMenu(request))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "메뉴 수정")
    public ApiResponse<?> updateMenu(@PathVariable Long id, @RequestBody MenuDto.MenuDtoRequest request) {
       return ResponseUtil.update(
               MenuDto.MenuDtoResponse.from(menuService.updateMenu(id, request))
       );
    }

}
