package com.aptner.v3.board.notices;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.notices.domain.NoticePost;
import com.aptner.v3.board.notices.dto.NoticePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class NoticePostService extends CommonPostService<
        NoticePost,
        NoticePostDto.NoticeRequest,
        NoticePostDto.NoticeResponse,
        NoticePostDto
        > {
    private final CommonPostRepository<NoticePost> commonPostRepository;

    public NoticePostService(CommonPostRepository<NoticePost> commonPostRepository, MemberRepository memberRepository, CommonPostRepository<NoticePost> commonPostRepository1) {
        super(commonPostRepository, memberRepository);
        this.commonPostRepository = commonPostRepository1;
    }

    public NoticePostDto.NoticeResponse createNoticePost(NoticePostDto.NoticeRequest requestDto) {
        if (!requestDto.getImageUrls().isEmpty()) {
            NoticePost entity = requestDto.toEntity();
            return new NoticePostDto.NoticeResponse(commonPostRepository.save(entity));
        }
        return new NoticePostDto.NoticeResponse(commonPostRepository.save(requestDto.toEntity()));
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
