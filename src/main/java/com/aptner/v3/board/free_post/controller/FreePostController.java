package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.FreePostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "자유 게시판")
@RequestMapping("/boards/frees")
@Slf4j
public class FreePostController extends CommonPostController<FreePost, FreePostDto.Request, FreePostDto.Response> {
    private final FreePostService freePostService;

    @PostMapping(value = "/attach")
    @Operation(summary = "첨부 파일 URL 업로드")
    public ResponseEntity<?> createPost(@RequestBody FreePostDto.Request requestDto) {
        log.info("@@@@@@ Controller.requestDto = " + requestDto.toString());
        return new ResponseEntity<>(freePostService.createPost(requestDto), HttpStatus.CREATED);
    }

    public FreePostController(
            CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> commonPostService,
            FreePostService freePostService) {
        super(commonPostService);
        this.freePostService = freePostService;
    }
}
