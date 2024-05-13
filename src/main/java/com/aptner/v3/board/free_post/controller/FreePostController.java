package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frees")
public class FreePostController extends CommonPostController<FreePost, FreePostDto.Request, FreePostDto.Response> {
    public FreePostController(CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> commonPostService) {
        super(commonPostService);
    }
}
