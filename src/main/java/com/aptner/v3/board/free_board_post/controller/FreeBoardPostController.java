package com.aptner.v3.board.free_board_post.controller;

import com.aptner.v3.board.free_board_post.service.FreeBoardPostService;
import com.aptner.v3.board.notice_post.service.NoticePostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/{menu-id}/categories/4/free-boards")
public class FreeBoardPostController {
    private final FreeBoardPostService freeBoardPostService;

    @GetMapping
    public ResponseEntity<?> getPostList(HttpServletRequest request) {
        return new ResponseEntity<>(freeBoardPostService.getPostList(request), HttpStatus.OK);
    }
}
