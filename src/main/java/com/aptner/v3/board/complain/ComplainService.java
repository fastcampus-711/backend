package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.board.complain.dto.ComplainStatusDto;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("complainService")
public class ComplainService extends CommonPostService<Complain, ComplainDto, ComplainDto.ComplainRequest, ComplainDto.ComplainResponse> {

    private final CommonPostRepository<Complain> complainRepository;

    public ComplainService(MemberRepository memberRepository,
                           CategoryRepository categoryRepository,
                           CommonPostRepository<Complain> complainRepository,
                           ReactionRepository<PostReaction> postReactionRepository
    ) {
        super(memberRepository, categoryRepository, complainRepository, postReactionRepository);
        this.complainRepository = complainRepository;
    }

    public ComplainDto setStatus(ComplainStatusDto dto) {
        // check
        Complain complain = complainRepository.findById(dto.getPostId()).orElseThrow(() -> new PostException(ErrorCode._NOT_FOUND));
        // update
        complain.setStatus(dto.getStatus());
        complainRepository.flush();
        return complain.toDto();
    }
}
