package com.aptner.v3.board.comment;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
