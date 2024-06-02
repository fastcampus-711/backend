package com.aptner.v3.board.comment;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.common_post.domain.SortType;
import com.aptner.v3.board.common_post.dto.SearchDto;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name="댓글")
@RequestMapping("/boards/{post-id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PaginationService paginationService;
    @PostMapping(value = {""})
    public ApiResponse<?> addComment(@PathVariable(name = "post-id") long postId,
                                     @AuthenticationPrincipal CustomUserDetails user,
                                     @RequestBody CommentDto.CommentRequest request) {

        CommentDto dto = request.toDto(postId, null, user);
        return ResponseUtil.ok(commentService.addComment(dto).toResponseDto());
    }

    @PutMapping(value = "/{comment-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateComment(@PathVariable("post-id") long postId,
                                        @AuthenticationPrincipal CustomUserDetails user,
                                        @PathVariable(name = "comment-id") long commentId,
                                        @RequestBody CommentDto.CommentRequest request) {

        CommentDto dto = request.toDto(postId, commentId, user);
        return ResponseUtil.update(commentService.updateComment(dto).toResponseDto());
    }

    @DeleteMapping("/{comment-id}")
    public ApiResponse<?> deleteComment(@PathVariable("post-id") long postId,
                                        @AuthenticationPrincipal CustomUserDetails user,
                                        @PathVariable(name = "comment-id") long commentId) {
        CommentDto dto = CommentDto.of(postId, user.toDto(), commentId);
        return ResponseUtil.delete(Collections.singletonMap("id", commentService.deleteComment(dto)));
    }

    @GetMapping("")
    @Operation(summary = "게시글 댓글 페이징")
    public ApiResponse<?> getPostWithComment(
            @PathVariable(name = "post-id") Long postId,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "sort", required = false, defaultValue = "RECENT") SortType sort,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
        Page<CommentDto> list = commentService.getPostWithComment(user.toDto(), postId, pageable);

        return ResponseUtil.ok(SearchDto.SearchResponse.from(SearchDto.of(
                list.map(p -> (CommentDto.CommentResponse) p.toResponseDto()),
                paginationService.getPaginationBarNumbers(pageable.getPageNumber(), list.getTotalPages())
        )));
    }
}
