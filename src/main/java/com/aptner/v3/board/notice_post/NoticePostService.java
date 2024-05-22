package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import org.springframework.stereotype.Service;

@Service
public class NoticePostService extends CommonPostService<NoticePost, NoticePostDto.Request, NoticePostDto.Response> {
    private final CommonPostRepository<NoticePost> commonPostRepository;
    public NoticePostService(CommonPostRepository<NoticePost> commonPostRepository) {
        super(commonPostRepository);
        this.commonPostRepository = commonPostRepository;
    }

    public NoticePostDto.Response createNoticePost(NoticePostDto.Request requestDto) {
        if (!requestDto.getImageUrls().isEmpty()) {
            NoticePost entity = requestDto.toEntity(requestDto.getImageUrls());
            return new NoticePostDto.Response(commonPostRepository.save(entity));
        }
        return new NoticePostDto.Response(commonPostRepository.save(requestDto.toEntity()));
    }

    /*public NoticePostDto.Response updatePost(long postId, NoticePostDto.Request requestDto) {
        NoticePost entity = commonPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        entity.update(requestDto);
        return new NoticePostDto.Response(commonPostRepository.save(entity));
    }

    public long deletePost(long postId) {
        NoticePost entity = commonPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        commonPostRepository.deleteById(postId);
        return entity.getId();
    }*/

}
