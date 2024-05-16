package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.NoticePostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "공지 사항")
@RequestMapping("/boards/notices")
public class NoticePostPostController extends CommonPostController<NoticePost, NoticePostDto.Request, NoticePostDto.Response> {
    public NoticePostPostController(CommonPostService<
            NoticePost,
            NoticePostDto.Request,
            NoticePostDto.Response> commonPostService,
                                    NoticePostService noticePostService) {
        super(commonPostService);
        this.noticePostService = noticePostService;
    }

    private final NoticePostService noticePostService;

    @PostMapping(value = "/attach")
    @Operation(summary = "첨부 파일 업로드")
    public ResponseEntity<?> createNoticePost(@RequestBody NoticePostDto.Request requestDto) {
        return new ResponseEntity<>(noticePostService.createNoticePost(requestDto), HttpStatus.CREATED);
    }
}
