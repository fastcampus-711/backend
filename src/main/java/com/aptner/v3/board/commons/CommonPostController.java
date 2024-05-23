package com.aptner.v3.board.commons;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.domain.SortType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class CommonPostController<E extends CommonPost,
        Q extends CommonPostDto.CommonRequest,
        S extends CommonPostDto.CommonResponse,
        T extends CommonPostDto> {
    private final CommonPostService<E, Q, S, T> commonPostService;

    /*@GetMapping("/categories")
    @Operation(summary = "게시판 조회")
    public ResponseEntity<?> getCategoryList(@PathVariable(name = "post-id") Long postId, HttpServletRequest request) {
        return new ResponseEntity<>(commonPostService.getCategoryList(postId,request), HttpStatus.OK);
    }*/

    @GetMapping("/{post-id}")
    @Operation(summary = "게시판 상세 조회")
    public ResponseEntity<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return new ResponseEntity<>(commonPostService.getPost(postId), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "게시판 조회")
    public ResponseEntity<?> getRequestMapper(@RequestParam(name = "keyword",required = false) String keyword,
                                              @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                              @RequestParam(name= "sort", required = false, defaultValue = "RECENT") SortType sort,
                                              HttpServletRequest request) {
        if (keyword == null)
            return getPostList(request,page);
        else
            return searchPost(request, keyword, limit, page, sort);
    }

    private ResponseEntity<?> getPostList(HttpServletRequest request, Integer page) {
        return new ResponseEntity<>(commonPostService.getPostList(request,page), HttpStatus.OK);
    }

    private ResponseEntity<?> searchPost(HttpServletRequest request, String keyword, Integer limit, Integer page, SortType sort) {
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

        return new ResponseEntity<>(commonPostService.deletePost(postId), HttpStatus.OK);
    }
}