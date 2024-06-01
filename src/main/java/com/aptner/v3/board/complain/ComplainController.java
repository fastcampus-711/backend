package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="민원 게시판")
@RequestMapping("/boards/complains")
public class ComplainController extends CommonPostController<
        Complain,
        ComplainDto,
        ComplainDto.ComplainRequest,
        ComplainDto.ComplainResponse> {
    ComplainService complainService;
    public ComplainController(@Qualifier("complainService") ComplainService complainService, PaginationService paginationService) {
        super(complainService, paginationService);
        this.complainService = complainService;
    }

    @Override
    public BoardGroup getBoardGroup() {
        return  BoardGroup.COMPLAINT;
    }
}
