package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class NoticePostService extends CommonPostService<NoticePost, NoticePostDto, NoticePostDto.NoticeRequest, NoticePostDto.NoticeResponse> {

    protected final CommonPostRepository<NoticePost> commonPostRepository;

    public NoticePostService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<NoticePost> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }
}
