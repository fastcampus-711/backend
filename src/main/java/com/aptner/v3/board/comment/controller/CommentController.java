package com.aptner.v3.board.comment.controller;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/{post-id}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable(name = "post-id") long postId, @RequestBody CommentDto.Request requestDto) {
        commentService.addComment(postId, requestDto);
        return new ResponseEntity<>("add comment success", HttpStatus.OK);
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "comment-id") long commentId, @RequestBody CommentDto.Request requestDto) {
        commentService.updateComment(commentId, requestDto);
        return new ResponseEntity<>("update comment success", HttpStatus.OK);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "comment-id") long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("delete comment success", HttpStatus.OK);
    }
}
