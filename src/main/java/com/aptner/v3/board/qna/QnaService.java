package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;
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

    private final QnaRepository qnaRepository;

    public QnaService(MemberRepository memberRepository,
                      CategoryRepository categoryRepository,
                      @Qualifier("qnaRepository") QnaRepository qnaRepository,
                      ReactionRepository<PostReaction> postReactionRepository,
                      ReactionRepository<CommentReaction> commentReactionRepository) {
        super(memberRepository, categoryRepository, qnaRepository, postReactionRepository, commentReactionRepository);
        this.qnaRepository = qnaRepository;
    }

    public QnaDto setStatus(QnaDto dto) {
        // check
        Qna qna = verifyPost(dto);
        // update
        qna.setStatus(dto.getStatus());
        // @todo test!!!! verify -> commonPost...
        qnaRepository.flush();
        return qna.toDto();
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

}
