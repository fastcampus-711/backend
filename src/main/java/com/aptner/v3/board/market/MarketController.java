package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.market.dto.MarketDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/markets")
public class MarketController extends CommonPostController<Market, MarketDto.Request, MarketDto.Response> {
    private final MarketService marketService;

    public MarketController(CommonPostService<Market, MarketDto.Request, MarketDto.Response> commonPostService,
                            MarketService marketService) {
        super(commonPostService);
        this.marketService = marketService;
    }

    @PostMapping(value = "/attach")
    @Operation(summary = "첨부 파일 업로드")
    public ResponseEntity<?> createPost(@RequestBody MarketDto.Request requestDto) {
        return new ResponseEntity<>(marketService.createPost(requestDto), HttpStatus.CREATED);
        //return ResponseEntity.ok(marketService.createPost(requestDto));
    }
}
