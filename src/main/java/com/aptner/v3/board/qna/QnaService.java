package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@Qualifier("qnaService")
public class QnaService extends CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.QnaResponse> {

    private final CommonPostRepository<Qna> commonPostRepository;

    public QnaService(MemberRepository memberRepository, CategoryRepository categoryRepository, CommonPostRepository<Qna> commonPostRepository) {
        super(memberRepository, categoryRepository, commonPostRepository);
        this.commonPostRepository = commonPostRepository;
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

        commonPostRepository.flush();
        log.debug("updatePost : {}", post);

        QnaDto postDto = (QnaDto) post.toDto();
        log.debug("createPost - postDto :{}", postDto);
        return postDto;
    }
}
