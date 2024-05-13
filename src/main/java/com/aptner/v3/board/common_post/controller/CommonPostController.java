package com.aptner.v3.board.common_post.controller;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.service.CommonPostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommonPostController<E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {
    private final CommonPostService<E, Q, S> commonPostService;

    @GetMapping("/{post-id}")
    @Operation(summary = "게시판 조회")
    public ResponseEntity<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return new ResponseEntity<>(commonPostService.getPost(postId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "게시판 조회")
    public ResponseEntity<?> getRequestMapper(@RequestParam(required = false) String keyword,
                                              @RequestParam(required = false, defaultValue = "10") Integer limit,
                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                              @RequestParam(required = false, defaultValue = "RECENT") SortType sort,
                                              HttpServletRequest request) {
        if (keyword == null)
            return getPostList(request);
        else
            return searchPost(request, keyword, limit, page, sort);
    }

    //자식 테이블에서만 정상 동작
    public ResponseEntity<?> getPostList(HttpServletRequest request) {
        return new ResponseEntity<>(commonPostService.getPost(request), HttpStatus.OK);
    }

    public ResponseEntity<?> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
        return new ResponseEntity<>(commonPostService.searchPost(request, keyword, limit, page, sort), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "게시판 등록")
    public ResponseEntity<?> createPost(@RequestBody Q requestDto) {
        return new ResponseEntity<>(commonPostService.createPost(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{post-id}")
    @Operation(summary = "게시판 수정")
    public ResponseEntity<?> updatePost(@PathVariable(name = "post-id") long postId, @RequestBody Q requestDto) {
        return new ResponseEntity<>(commonPostService.updatePost(postId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시판 삭제")
    public ResponseEntity<?> deletePost(@PathVariable(name = "post-id") long postId) {
        commonPostService.deletePost(postId);
        return new ResponseEntity<>("delete post success", HttpStatus.OK);
    }
}