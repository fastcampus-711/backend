package com.aptner.v3.board.complains;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.complains.dto.ComplainDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="민원 게시판")
@RequestMapping("/boards/qnas")
public class ComplainController extends CommonPostController<
        ComplainPost,
        ComplainDto.ComplainRequest,
        ComplainDto.ComplainResponse,
        ComplainDto
        > {

    public ComplainController(CommonPostService<ComplainPost, ComplainDto.ComplainRequest, ComplainDto.ComplainResponse, ComplainDto> commonPostService) {
        super(commonPostService);
    }
}
