package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frees")
public class FreePostController extends CommonPostController<FreePost> {
    public FreePostController(CommonPostService<FreePost> commonPostService) {
        super(commonPostService);
    }
}
