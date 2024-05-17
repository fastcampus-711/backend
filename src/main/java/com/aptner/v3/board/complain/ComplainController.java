package com.aptner.v3.board.complain;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/qnas")
public class ComplainController extends CommonPostController<Complain, ComplainDto.Request, ComplainDto.Response> {
    public ComplainController(CommonPostService<Complain, ComplainDto.Request, ComplainDto.Response> commonPostService) {
        super(commonPostService);
    }
}
