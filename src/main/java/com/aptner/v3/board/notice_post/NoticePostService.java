package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("noticePostService")
public class NoticePostService extends CommonPostService<NoticePost, NoticePostDto, NoticePostDto.NoticeRequest, NoticePostDto.NoticeResponse> {

    protected final CommonPostRepository<NoticePost> commonPostRepository;

    public NoticePostService(MemberRepository memberRepository,
                             CategoryRepository categoryRepository,
                             CommonPostRepository<NoticePost> commonPostRepository,
                             ReactionRepository<PostReaction> postReactionRepository,
                             ReactionRepository<CommentReaction> commentReactionRepository) {
        super(memberRepository, categoryRepository, commonPostRepository, postReactionRepository,commentReactionRepository);
        this.commonPostRepository = commonPostRepository;
    }
}
