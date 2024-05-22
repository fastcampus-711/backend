package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aptner.v3.global.config.SwaggerConfig.Accesskey;

@RestController
@Tag(name="자유 게시판")
@SecurityRequirement(name = Accesskey)
@RequestMapping("/boards/frees")
public class FreePostController extends CommonPostController<FreePost, FreePostDto.Request, FreePostDto.Response> {
    public FreePostController(CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> commonPostService) {
        super(commonPostService);
    }
}
