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

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable(name = "post-id") long postId) {
        return new ResponseEntity<> (commentService.getComments(postId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable(name = "post-id") long postId, @RequestBody CommentDto.AddRequest requestDto) {
        commentService.addComment(postId, requestDto);
        return new ResponseEntity<>("add comment success", HttpStatus.OK);
    }

}
