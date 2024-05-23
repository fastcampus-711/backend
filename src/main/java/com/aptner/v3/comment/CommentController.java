package com.aptner.v3.comment;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name="댓글")
@RequestMapping("/boards/{post-id}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{comment-id}")
    @Operation(summary = "댓글 생성")
    public ApiResponse<?> addComment(@PathVariable(name = "post-id") long postId,
                                        @RequestBody CommentDto.CommentRequest commentRequestDto) {
        log.debug("postId : {}", postId);
        return ResponseUtil.create(commentService.addComment(postId, commentRequestDto));
    }

    @PutMapping(value = "/{comment-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글 수정")
    public ApiResponse<?> updateComment(@PathVariable("post-id") long postId,
                                        @PathVariable(name = "comment-id") long commentId,
                                        @RequestBody CommentDto.CommentRequest commentRequestDto) {
        log.debug("postId : {}", postId);
        return ResponseUtil.update(commentService.updateComment(postId, commentId, commentRequestDto));
    }

    @DeleteMapping("/{comment-id}")
    @Operation(summary = "댓글 삭제")
    public ApiResponse<?> deleteComment(@PathVariable("post-id") long postId,
                                        @PathVariable(name = "comment-id") long commentId) {
        return ResponseUtil.delete(commentService.deleteComment(postId, commentId));
    }
}
