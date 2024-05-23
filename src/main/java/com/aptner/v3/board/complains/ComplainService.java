package com.aptner.v3.board.complains;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.complains.dto.ComplainDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ComplainService extends CommonPostService<
        ComplainPost,
        ComplainDto.ComplainRequest,
        ComplainDto.ComplainResponse,
        ComplainDto
        > {
    public ComplainService(CommonPostRepository<ComplainPost> commonPostRepository, MemberRepository memberRepository) {
        super(commonPostRepository, memberRepository);
    }

}
