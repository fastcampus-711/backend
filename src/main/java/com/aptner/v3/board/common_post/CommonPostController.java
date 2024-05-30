package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class CommonPostController<E extends CommonPost,
        Q extends CommonPostDto.Request,
        S extends CommonPostDto.Response> {

    protected final CommonPostService<E, Q, S> commonPostService;

    @GetMapping("")
    @Operation(summary = "게시글 검색")
    public ApiResponse<?> getPostListByCategoryId(@RequestParam(name = "category-id", defaultValue = "0") Long categoryId,
                                                  @RequestParam(name = "withPopular", required = false) Boolean withPopular,
                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort
    ) {

        BoardGroup boardGroup = getBoardGroup();
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        if (keyword != null) {
            log.debug("keyword search : boardGroup: {}, categoryId: {}, keyword: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, keyword, limit, page, sort);
            return ResponseUtil.ok(commonPostService.getPostListByCategoryIdAndTitle(boardGroup, categoryId, keyword, pageable));
        }
        log.debug("category search : boardGroup: {},  categoryId: {}, limit: {}, page: {}, sort: {}", boardGroup, categoryId, limit, page, sort);
        return ResponseUtil.ok(commonPostService.getPostListByCategoryId(boardGroup, categoryId, pageable));
    }

    @GetMapping("/{post-id}")
    @Operation(summary = "게시글 상세")
    public ApiResponse<?> getPost(@PathVariable(name = "post-id") Long postId) {
        return ResponseUtil.ok(commonPostService.getPost(postId));
    }

    @PostMapping("/")
    @Operation(summary = "게시글 등록")
    public ApiResponse<?> createPost(
            @RequestBody Q requestDto
    ) {

        return ResponseUtil.create(commonPostService.createPost(requestDto));
    }

    @PutMapping("/{post-id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<?> updatePost(HttpServletRequest request, @PathVariable(name = "post-id") long postId, @RequestBody Q requestDto) {
        return ResponseUtil.update(commonPostService.updatePost(request, postId, requestDto));
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<?> deletePost(HttpServletRequest request, @PathVariable(name = "post-id") long postId) {

        long deleted = commonPostService.deletePost(request, postId);
        return ResponseUtil.delete(deleted);
    }

    public BoardGroup getBoardGroup() {
        return BoardGroup.ALL;
    }
}