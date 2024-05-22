package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.qnas.dto.QnaDto;

public class QnaService extends CommonPostService<Qna, QnaDto.QnaRequest, QnaDto.QnaResponse> {
    public QnaService(CommonPostRepository<Qna> commonPostRepository) {
        super(commonPostRepository);
    }
}
