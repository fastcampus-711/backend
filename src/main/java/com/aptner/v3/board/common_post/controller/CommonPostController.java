package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommonPostController<T extends CommonPost> {
    private final CommonPostService<T> commonPostService;

    @GetMapping("/{post-id}")
    public ResponseEntity<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return new ResponseEntity<>(commonPostService.getPost(postId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getPostList(HttpServletRequest request) {
        return new ResponseEntity<>(commonPostService.getPost(request), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody NoticePostDto.CreateRequest requestDto) {
        commonPostService.createPost(requestDto);
        return new ResponseEntity<>("create post success", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestBody NoticePostDto.UpdateRequest requestDto) {
        commonPostService.updatePost(requestDto);
        return new ResponseEntity<>("update post success", HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "post-id") long postId) {
        commonPostService.deletePost(postId);
        return new ResponseEntity<>("delete post success", HttpStatus.OK);
    }
}
