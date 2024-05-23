package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class CommonPostController<E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {
    private final CommonPostService<E, Q, S> commonPostService;

    public CommonPostController(CommonPostService<E, Q, S> commonPostService) {
        this.commonPostService = commonPostService;
    }

    @GetMapping("/categories/{category-id}")
    @Operation(summary = "분류 선택 게시판 조회")
    public ApiResponse<?> getPostListByCategoryId(@PathVariable(name = "category-id") Long categoryId,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort,
                                                  HttpServletRequest request) {
        return ResponseUtil.ok(commonPostService.getPostListByCategoryId(categoryId, request, limit, page, sort));
    }

    @GetMapping("/{post-id}")
    @Operation(summary = "게시판 조회")
    public ApiResponse<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return ResponseUtil.create(commonPostService.getPost(postId));
    }

    @GetMapping
    @Operation(summary = "게시판 조회")
    public ApiResponse<?> getRequestMapper(@RequestParam(name = "keyword", required = false) String keyword,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                           @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort,
                                           HttpServletRequest request) {
        if (keyword == null)
            return ResponseUtil.ok(commonPostService.getPostList(request, limit, page, sort));
        else
            return ResponseUtil.ok(commonPostService.searchPost(request, keyword, limit, page, sort));
    }

    @PostMapping
    @Operation(summary = "게시판 등록")
    public ApiResponse<?> createPost(@RequestBody Q requestDto) {
        return ResponseUtil.create(commonPostService.createPost(requestDto));
    }

    @PutMapping("/{post-id}")
    @Operation(summary = "게시판 수정")
    public ApiResponse<?> updatePost(HttpServletRequest request, @PathVariable(name = "post-id") long postId, @RequestBody Q requestDto) {
        return ResponseUtil.update(commonPostService.updatePost(request, postId, requestDto));
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시판 삭제")
    public ApiResponse<?> deletePost(HttpServletRequest request, @PathVariable(name = "post-id") long postId) {

        return ResponseUtil.delete(commonPostService.deletePost(request, postId));
    }
}