package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
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

    public ComplainDto setStatus(ComplainDto dto) {
        Complain complain = verifyPost(dto);
        // update
        complain.setStatus(dto.getStatus());
        complainRepository.flush();
        return complain.toDto();
    }
}
