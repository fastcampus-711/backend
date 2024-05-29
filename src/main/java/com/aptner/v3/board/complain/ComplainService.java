package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ComplainService extends CommonPostService<Complain, ComplainDto, ComplainDto.ComplainRequest, ComplainDto.ComplainResponse> {


    public ComplainService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Complain> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
    }
}
