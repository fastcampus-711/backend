package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import org.springframework.stereotype.Service;

@Service
public class NoticePostService extends CommonPostService<NoticePost, NoticePostDto.Request, NoticePostDto.Response> {

    public NoticePostService(CommonPostRepository<NoticePost> commonPostRepository) {
        super(commonPostRepository);
    }

    public NoticePostDto.Response createNoticePost(NoticePostDto.Request requestDto) {
        if (!requestDto.getImageUrls().isEmpty()){
            NoticePost entity = requestDto.toEntity(requestDto.getImageUrls());
            return new NoticePostDto.Response(commonPostRepository.save(entity));
        }
        return new NoticePostDto.Response(commonPostRepository.save(requestDto.toEntity()));
    }
}
