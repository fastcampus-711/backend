package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.qnas.dto.QnaDto;

public class QnaController extends CommonPostController<
        QnaPost,
        QnaDto.QnaRequest,
        QnaDto.QnaResponse,
        QnaDto
        > {
    public QnaController(CommonPostService<QnaPost, QnaDto.QnaRequest, QnaDto.QnaResponse, QnaDto> commonPostService) {
        super(commonPostService);
    }
}
