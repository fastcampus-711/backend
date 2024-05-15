package com.aptner.v3.board.qna;

import com.aptner.v3.board.common_post.controller.CommonPostController;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;

public class QnaController extends CommonPostController<Qna, QnaDto.Request, QnaDto.Response> {
    public QnaController(CommonPostService<Qna, QnaDto.Request, QnaDto.Response> commonPostService) {
        super(commonPostService);
    }
}
