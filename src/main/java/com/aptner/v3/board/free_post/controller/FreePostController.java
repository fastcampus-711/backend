package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.free_post.FreePostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aptner.v3.global.config.SwaggerConfig.Accesskey;

@RestController
@Tag(name="자유 게시판")
@SecurityRequirement(name = Accesskey)
@RequestMapping("/boards/frees")
@Slf4j
public class FreePostController extends CommonPostController<FreePost, FreePostDto.Request, FreePostDto.Response> {
    private final FreePostService freePostService;

    public FreePostController(
            CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> commonPostService,
            FreePostService freePostService) {
        super(commonPostService);
        this.freePostService = freePostService;
    }

}
