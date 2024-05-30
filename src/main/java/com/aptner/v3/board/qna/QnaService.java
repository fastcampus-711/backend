package com.aptner.v3.board.qna;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.dto.MarketDto;

public class QnaService extends CommonPostService<Market, MarketDto.Request, MarketDto.Response> {
    public QnaService(CommonPostRepository<Market> commonPostRepository,
                      ReactionRepository<PostReaction> postReactionRepository,
                      ReactionRepository<CommentReaction> commentReactionRepository) {
        super(commonPostRepository, postReactionRepository, commentReactionRepository);
    }
}
