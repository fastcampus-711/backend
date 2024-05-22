package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.qnas.dto.QnaDto;

public class QnaController extends CommonPostController<Qna, QnaDto.QnaRequest, QnaDto.QnaResponse> {
    public QnaController(CommonPostService<Qna, QnaDto.QnaRequest, QnaDto.QnaResponse> commonPostService) {
        super(commonPostService);
    }
}
