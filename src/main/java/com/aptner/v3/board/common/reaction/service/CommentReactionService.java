package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.comment.service.CommentService;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService extends ReactionService<Comment, CommentReaction> {
    public CommentReactionService(CommentService commentService,
                                  ReactionRepository<CommentReaction> reactionRepository) {
        super(commentService, reactionRepository);
    }
}
