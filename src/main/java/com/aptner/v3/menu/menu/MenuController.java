package com.aptner.v3.menu.menu;

import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.menu.menu.dto.MenuDto;
import com.aptner.v3.global.error.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="상위 메뉴")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "상위 메뉴 조회")
    public ApiResponse<?> getMenuList() {
        return ResponseUtil.ok(menuService.getMenuList());
    }

    @PostMapping
    @Operation(summary = "상위 메뉴 생성")
    public ApiResponse<?> createMenu(@RequestBody MenuDto.MenuRequest request) {
        return ResponseUtil.create(menuService.createMenu(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상위 메뉴 삭제")
    public ApiResponse<?> deleteMenu(@PathVariable Long id) {
        return ResponseUtil.delete(menuService.deleteMenu(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "상위 메뉴 수정")
    public ApiResponse<?> updateMenu(@PathVariable Long id, @RequestBody MenuDto.MenuRequest menuRequest) {
       return ResponseUtil.update(menuService.updateMenu(id, menuRequest));
    }

    @GetMapping("/{id}/category")
    @Operation(summary = "상위 메뉴 별 게시판 조회")
    public ResponseEntity<?> getCategoryList(@PathVariable("id") long menuId) {
        return new ResponseEntity<>(menuService.getCategoryList(menuId), HttpStatus.OK);
    }

}
