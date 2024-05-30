package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.member.repository.MemberRepository;

public class QnaService extends CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.QnaResponse> {

    private final CommonPostRepository<Qna> commonPostRepository;

    public QnaService(MemberRepository memberRepository,
                      CategoryRepository categoryRepository,
                      CommonPostRepository<Qna> commonPostRepository,
                      ReactionRepository<PostReaction> postReactionRepository,
                      ReactionRepository<CommentReaction> commentReactionRepository) {
        super(memberRepository, categoryRepository, commonPostRepository, postReactionRepository, commentReactionRepository);
        this.commonPostRepository = commonPostRepository;
    }
}
