package com.aptner.v3.board.free_post;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<FreePost, FreePostDto, FreePostDto.FreePostRequest, FreePostDto.FreePostResponse> {

    private final CommonPostRepository<FreePost> commonPostRepository;

    public FreePostService(MemberRepository memberRepository,
                           CategoryRepository categoryRepository,
                           CommonPostRepository<FreePost> commonPostRepository,
                           ReactionRepository<PostReaction> postReactionRepository,
                           ReactionRepository<CommentReaction> commentReactionRepository) {
        super(memberRepository, categoryRepository, commonPostRepository, postReactionRepository, commentReactionRepository);
        this.commonPostRepository = commonPostRepository;
    }
}
