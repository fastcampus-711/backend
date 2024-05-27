package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.notice_post.NoticePostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="공지 사항")
@RequestMapping("/boards/notices")
public class NoticePostController extends CommonPostController<
        NoticePost,
        NoticePostDto,
        NoticePostDto.Request,
        NoticePostDto.Response> {
    private final NoticePostService noticePostService;

    protected BoardGroup boardGroup = BoardGroup.NOTICES;

    public NoticePostController(NoticePostService noticePostService) {
        super(noticePostService);
        this.noticePostService = (NoticePostService) commonPostService;
    }
//    @PostMapping(value = "/attach")
//    @Operation(summary = "첨부 파일 업로드")
//    public ResponseEntity<?> createNoticePost(@RequestBody NoticePostDto.Request requestDto) {
//        return new ResponseEntity<>(noticePostService.createNoticePost(requestDto), HttpStatus.CREATED);
//    }

    /*@PutMapping("/{post-id}/update")
    @Operation(summary = "게시판 수정")
    public ResponseEntity<?> updateNoticePost(@PathVariable(name = "post-id") long postId, @RequestBody NoticePostDto.Request requestDto) {
        return new ResponseEntity<>(noticePostService.updatePost(postId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시판 삭제")
    public ResponseEntity<?> deletePost(@PathVariable(name = "post-id") long postId) {
        return new ResponseEntity<>(noticePostService.deletePost(postId), HttpStatus.OK);
    }*/
}
