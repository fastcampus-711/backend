package com.aptner.v3.board.qna;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.board.qna.dto.QnaStatusDto;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("qnaService")
public class QnaService extends CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.QnaResponse> {

    QnaRepository qnaRepository;
    public QnaService(MemberRepository memberRepository, CategoryRepository categoryRepository, @Qualifier("qnaRepository") QnaRepository qnaRepository) {
        super(memberRepository, categoryRepository, qnaRepository);
        this.qnaRepository = qnaRepository;
    }

    @Override
    public Page<Qna> findByDtypeAndStatus(BoardGroup boardGroup, Status status, Pageable pageable) {
        return qnaRepository.findByDtypeAndStatus(boardGroup.getTable(), (QnaStatus) status, pageable);
    }

    @Override
    public Page<Qna> findByDtypeAndCategoryIdAndStatus(BoardGroup boardGroup, Long categoryId, Status status, Pageable pageable) {
        return qnaRepository.findByDtypeAndCategoryIdAndStatus(boardGroup.getTable(), categoryId, (QnaStatus) status, pageable);
    }

    @Override
    public QnaDto updatePost(QnaDto dto) {

        log.debug("updatePost : dto {}", dto);
        Member member = verifyMember(dto);
        Category category = verifyCategory(dto);
        Qna post = verifyPost(dto);

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getImageUrls() != null) {
            post.setImageUrls(dto.getImageUrls());
        }

        qnaRepository.flush();
        log.debug("updatePost : {}", post);

        QnaDto postDto = (QnaDto) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }

    public QnaDto setStatus(CustomUserDetails user, QnaStatusDto.QnaStatusRequest request) {
        // check
        Qna qna = qnaRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostException(ErrorCode._NOT_FOUND));
        // update
        qna.setStatus(request.getStatus());
        qnaRepository.flush();
        return qna.toDto();
    }
}
