package com.aptner.v3.board.complain;

import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="민원 게시판")
@RequestMapping("/boards/qnas")
public class ComplainController extends CommonPostController<Complain, ComplainDto.Request, ComplainDto.Response> {
    public ComplainController(CommonPostService<Complain, ComplainDto.Request, ComplainDto.Response> commonPostService) {
        super(commonPostService);
    }
}
