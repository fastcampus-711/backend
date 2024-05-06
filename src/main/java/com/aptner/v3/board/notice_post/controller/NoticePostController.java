package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.notice_post.domain.NoticePost;
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
@RequestMapping("/menus/{menu-id}/categories/2/notices")
public class NoticePostController {
    private final NoticePostService noticePostService;

    @GetMapping
    public ResponseEntity<?> getPostList(HttpServletRequest request) {
        return new ResponseEntity<>(noticePostService.getPostList(request), HttpStatus.OK);
    }
}
