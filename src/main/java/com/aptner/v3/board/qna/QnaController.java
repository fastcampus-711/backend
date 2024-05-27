package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.CommonPostController;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;

public class QnaController extends CommonPostController<
        Qna,
        QnaDto,
        QnaDto.Request,
        QnaDto.Response> {

    protected BoardGroup boardGroup = BoardGroup.QNAS;

    public QnaController(CommonPostService<Qna, QnaDto, QnaDto.Request, QnaDto.Response> commonPostService) {
        super(commonPostService);
    }
}
