package com.aptner.v3.board.free_post;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<FreePost, FreePostDto, FreePostDto.CommonPostRequest, FreePostDto.CommonPostResponse> {

    public FreePostService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<FreePost> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
    }
}
