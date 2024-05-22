package com.aptner.v3.reaction.service;

import com.aptner.v3.reaction.ReactionRepository;
import com.aptner.v3.reaction.domain.PostReaction;
import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.domain.CommonPost;
import org.springframework.stereotype.Service;

@Service
public class PostReactionService extends ReactionService<CommonPost, PostReaction> {
    public PostReactionService(CommonPostRepository<CommonPost> countOfReactionAndCommentApplyService,
                               ReactionRepository<PostReaction> reactionRepository) {
        super(countOfReactionAndCommentApplyService, reactionRepository);
    }
}
