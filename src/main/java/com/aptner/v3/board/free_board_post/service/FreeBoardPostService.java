package com.aptner.v3.board.free_board_post.service;

import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.free_board_post.domain.FreeBoardPost;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import org.springframework.stereotype.Service;

@Service
public class FreeBoardPostService extends CommonPostService<FreeBoardPost> {
    public FreeBoardPostService(CommonPostRepository<FreeBoardPost> commonPostRepository) {
        super(commonPostRepository);
    }
}
