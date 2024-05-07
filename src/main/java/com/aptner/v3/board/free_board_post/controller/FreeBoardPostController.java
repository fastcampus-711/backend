package com.aptner.v3.board.free_board_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_board_post.domain.FreeBoardPost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/free-boards")
public class FreeBoardPostController extends CommonPostController<FreeBoardPost> {
    public FreeBoardPostController(CommonPostService<FreeBoardPost> commonPostService) {
        super(commonPostService);
    }
}
