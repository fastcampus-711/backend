package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostService;
import org.springframework.stereotype.Service;

@Service
public class PostReactionService extends ReactionService<CommonPost, PostReaction> {
    public PostReactionService(CommonPostRepository<CommonPost> countOfReactionAndCommentApplyService,
                               ReactionRepository<PostReaction> reactionRepository) {
        super(countOfReactionAndCommentApplyService, reactionRepository);
    }
}
