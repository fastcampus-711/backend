package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.service.PaginationService;
import com.aptner.v3.board.qna.dto.QnaDto;

public class QnaController extends CommonPostController<
        Qna,
        QnaDto,
        QnaDto.QnaRequest,
        QnaDto.CommonPostResponse> {

    public QnaController(CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.CommonPostResponse> commonPostService, PaginationService paginationService) {
        super(commonPostService, paginationService);
    }

    @Override
    public BoardGroup getBoardGroup() {
        return BoardGroup.QNAS;
    }
}
