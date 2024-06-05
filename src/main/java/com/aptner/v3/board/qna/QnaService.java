package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.report.ReportRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.aptner.v3.board.qna.dto.QnaStatusDto;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@Qualifier("qnaService")
public class QnaService extends CommonPostService<Qna, QnaDto, QnaDto.QnaRequest, QnaDto.QnaResponse> {

    private final QnaRepository qnaRepository;

    public QnaService(MemberRepository memberRepository,
                      CategoryRepository categoryRepository,
                      @Qualifier("qnaRepository") QnaRepository qnaRepository,
                      ReactionRepository<PostReaction> postReactionRepository,
                      ReportRepository reportRepository
    ) {
        super(memberRepository, categoryRepository, qnaRepository, postReactionRepository, reportRepository);
        this.qnaRepository = qnaRepository;
    }

    public QnaDto setStatus(QnaStatusDto dto) {
        // check
        Qna qna = qnaRepository.findById(dto.getPostId()).orElseThrow(() -> new PostException(ErrorCode._NOT_FOUND));
        // save
        boolean commentExists = containsCommentId(qna.getComments(), dto.getCommentId());
        if (commentExists) {
            qna.setStatus(dto.getStatus());
            // 코멘트 상단 고정
            for (Comment comment : qna.getComments()) {
                if (comment.getId().equals(dto.getCommentId())) {
                    comment.setTop(true); // Assuming Comment has a setIsTop method
                    break;
                }
            }
            qnaRepository.flush();
            return qna.toDto();
        } else {
            throw new RuntimeException("코멘트를 찾지 못하였습니다.");
        }
    }

    private boolean containsCommentId(Set<Comment> comments, Long commentId) {
        return comments.stream().anyMatch(comment -> comment.getId().equals(commentId));
    }

    @Override
    protected Qna verifyDeletePost(QnaDto dto) {
        Qna qna = super.verifyDeletePost(dto);
        if (qna.getStatus() == QnaStatus.RESPONSE_ACCEPTED) {
            throw new PostException(ErrorCode.DELETE_NOT_AVAILABLE);
        }

        return qna;
    }
}
