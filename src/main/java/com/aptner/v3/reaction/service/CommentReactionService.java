package com.aptner.v3.reaction.service;

import com.aptner.v3.comment.Comment;
import com.aptner.v3.comment.CommentRepository;
import com.aptner.v3.reaction.ReactionRepository;
import com.aptner.v3.reaction.domain.CommentReaction;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService extends ReactionService<Comment, CommentReaction> {
    public CommentReactionService(CommentRepository commentRepository,
                                  ReactionRepository<CommentReaction> reactionRepository) {
        super(commentRepository, reactionRepository);
    }
}
