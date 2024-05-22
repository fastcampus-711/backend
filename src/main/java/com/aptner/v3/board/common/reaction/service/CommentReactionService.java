package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.comment.CommentService;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService extends ReactionService<Comment, CommentReaction> {
    public CommentReactionService(CommentRepository commentRepository,
                                  ReactionRepository<CommentReaction> reactionRepository) {
        super(commentRepository, reactionRepository);
    }
}
