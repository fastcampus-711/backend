package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostController;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.qnas.dto.QnaDto;

public class QnaController extends CommonPostController<Qna, QnaDto.Request, QnaDto.Response> {
    public QnaController(CommonPostService<Qna, QnaDto.Request, QnaDto.Response> commonPostService) {
        super(commonPostService);
    }
}
