package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.qnas.dto.QnaDto;
import com.aptner.v3.member.repository.MemberRepository;

public class QnaService extends CommonPostService<
        QnaPost,
        QnaDto.QnaRequest,
        QnaDto.QnaResponse,
        QnaDto> {
    public QnaService(CommonPostRepository<QnaPost> commonPostRepository, MemberRepository memberRepository) {
        super(commonPostRepository, memberRepository);
    }
}
