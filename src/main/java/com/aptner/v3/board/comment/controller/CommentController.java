package com.aptner.v3.board.comment.controller;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.comment.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name="댓글")
@RequestMapping("/boards/{post-id}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComment() {
        return new ResponseEntity<>(commentService.getComment(), HttpStatus.OK);
    }

    @PostMapping(value = {"/{comment-id}", ""})
    public ResponseEntity<?> addComment(@PathVariable(name = "post-id") long postId,
                                        @PathVariable(name = "comment-id", required = false) Long commentId,
                                        @RequestBody CommentDto.Request requestDto) {
        return new ResponseEntity<>(commentService.addComment(postId, commentId, requestDto), HttpStatus.OK);
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "comment-id") long commentId, @RequestBody CommentDto.Request requestDto) {
        return new ResponseEntity<>(commentService.updateComment(commentId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "comment-id") long commentId) {

        return new ResponseEntity<>(commentService.deleteComment(commentId), HttpStatus.OK);
    }
}
