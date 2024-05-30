package com.aptner.v3.board.market;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="나눔 게시판")
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<
        Market,
        MarketDto.Request,
        MarketDto.Response> {
    private final MarketService marketService;

    protected BoardGroup boardGroup = BoardGroup.MARKETS;

    public MarketController(CommonPostService<Market, MarketDto.Request, MarketDto.Response> commonPostService,
                            MarketService marketService) {
        super(commonPostService);
        this.marketService = marketService;
    }

    @PostMapping(value = "/attach")
    @Operation(summary = "첨부 파일 업로드")
    public ApiResponse<?> createPost(@RequestBody MarketDto.Request requestDto) {
        return ResponseUtil.create(marketService.createPost(requestDto));
        //return ResponseEntity.ok(marketService.createPost(requestDto));
    }

    /*@PutMapping("/{post-id}/update")
    @Operation(summary = "게시판 수정")
    public ResponseEntity<?> updateMarketPost(@PathVariable(name = "post-id") long postId, @RequestBody MarketDto.Request requestDto) {
        return new ResponseEntity<>(marketService.updatePost(postId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시판 삭제")
    public ResponseEntity<?> deletePost(@PathVariable(name = "post-id") long postId) {
        return new ResponseEntity<>(marketService.deletePost(postId), HttpStatus.OK);
    }*/
}
