package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.service.CommonPostService;
import org.springframework.stereotype.Service;

@Service
public class PostReactionService extends ReactionService<CommonPost, PostReaction> {
    public PostReactionService(CommonPostService<CommonPost, CommonPostDto.Request, CommonPostDto.Response> commonPostService,
                               ReactionRepository<PostReaction> reactionRepository) {
        super(commonPostService, reactionRepository);
    }
}
