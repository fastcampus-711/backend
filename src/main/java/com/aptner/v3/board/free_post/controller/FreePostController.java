package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aptner.v3.global.config.SwaggerConfig.Accesskey;

@RestController
@Tag(name="자유 게시판")
@SecurityRequirement(name = Accesskey)
@RequestMapping("/boards/frees")
@Slf4j
public class FreePostController extends CommonPostController<
        FreePost,
        FreePostDto,
        FreePostDto.CommonPostRequest,
        FreePostDto.CommonPostResponse> {

    public FreePostController(CategoryRepository categoryRepository, CommonPostService<FreePost, FreePostDto, FreePostDto.CommonPostRequest, FreePostDto.CommonPostResponse> commonPostService, PaginationService paginationService) {
        super(categoryRepository, commonPostService, paginationService);
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.FREES;
    }
}
