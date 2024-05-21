package com.aptner.v3.board.free_post.controller;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.FreePostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
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

    @PostMapping(value = "/attach")
    @Operation(summary = "첨부 파일 URL 업로드")
    public ResponseEntity<?> createPost(@RequestBody FreePostDto.Request requestDto) {
        log.info("@@@@@@ Controller.requestDto = " + requestDto.toString());
        return new ResponseEntity<>(freePostService.createPost(requestDto), HttpStatus.CREATED);
    }

    /*@PutMapping("/{post-id}/update")
    @Operation(summary = "게시판 수정")
    public ResponseEntity<?> updateFreePost(@PathVariable(name = "post-id") long postId, @RequestBody FreePostDto.Request requestDto) {
        return new ResponseEntity<>(freePostService.updatePost(postId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시판 삭제")
    public ResponseEntity<?> deletePost(@PathVariable(name = "post-id") long postId) {
        return new ResponseEntity<>(freePostService.deletePost(postId), HttpStatus.OK);
    }*/

    public FreePostController(
            CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> commonPostService,
            FreePostService freePostService) {
        super(commonPostService);
        this.freePostService = freePostService;
    }
}
