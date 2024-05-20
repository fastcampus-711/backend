package com.aptner.v3.board.comment;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name="댓글")
@RequestMapping("/boards/{post-id}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = {"/{comment-id}", ""})
    public ApiResponse<?> addComment(@PathVariable(name = "post-id") long postId,
                                        @PathVariable(name = "comment-id", required = false) Long commentId,
                                        @RequestBody CommentDto.Request requestDto) {
        return ResponseUtil.ok(commentService.addComment(postId, commentId, requestDto));
    }

    @PutMapping("/{comment-id}")
    public ApiResponse<?> updateComment(@PathVariable("post-id") long postId,
                                        @PathVariable(name = "comment-id") long commentId,
                                        @RequestBody CommentDto.Request requestDto) {
        return ResponseUtil.update(commentService.updateComment(postId, commentId, requestDto));
    }

    @DeleteMapping("/{comment-id}")
    public ApiResponse<?> deleteComment(@PathVariable("post-id") long postId,
                                        @PathVariable(name = "comment-id") long commentId) {
        return ResponseUtil.delete(commentService.deleteComment(postId, commentId));
    }
}
