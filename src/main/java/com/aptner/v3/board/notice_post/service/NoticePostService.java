package com.aptner.v3.board.notice_post.service;

import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import org.springframework.stereotype.Service;

@Service
public class NoticePostService extends CommonPostService<NoticePost> {
    public NoticePostService(CommonPostRepository<NoticePost> commonPostRepository) {
        super(commonPostRepository);
    }
}
