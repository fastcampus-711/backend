package com.aptner.v3.board.notice_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
public class NoticePostPostController extends CommonPostController<NoticePost> {
    public NoticePostPostController(CommonPostService<NoticePost> commonPostService) {
        super(commonPostService);
    }
}
