package com.aptner.v3.board.frees.controller;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.frees.FreePostService;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.frees.dto.FreePostDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aptner.v3.global.config.SwaggerConfig.Accesskey;

@Slf4j
@RestController
@Tag(name="자유 게시판")
@SecurityRequirement(name = Accesskey)
@RequestMapping("/boards/frees")
public class FreePostController extends CommonPostController<
        FreePost,
        FreePostDto.FreeCommonRequest,
        FreePostDto.FreeCommonResponse,
        FreePostDto
        > {

    private FreePostService freePostService;

    public FreePostController(CommonPostService<FreePost, FreePostDto.FreeCommonRequest, FreePostDto.FreeCommonResponse, FreePostDto> commonPostService, FreePostService freePostService) {
        super(commonPostService);
        this.freePostService = freePostService;
    }
}