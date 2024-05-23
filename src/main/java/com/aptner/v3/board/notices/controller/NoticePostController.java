package com.aptner.v3.board.notices.controller;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.notices.domain.NoticePost;
import com.aptner.v3.board.notices.dto.NoticePostDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "공지 사항")
@RequestMapping("/boards/notices")
public class NoticePostController extends CommonPostController<
        NoticePost,
        NoticePostDto.NoticeRequest,
        NoticePostDto.NoticeResponse,
        NoticePostDto
        > {

    public NoticePostController(CommonPostService<NoticePost, NoticePostDto.NoticeRequest, NoticePostDto.NoticeResponse, NoticePostDto> commonPostService) {
        super(commonPostService);
    }
}
