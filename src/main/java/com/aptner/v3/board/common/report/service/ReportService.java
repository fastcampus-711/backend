package com.aptner.v3.board.common.report.service;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.common.report.ReportRepository;
import com.aptner.v3.board.common.report.dto.ReportDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CommentException;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final MemberRepository memberRepository;

    private final CommonPostRepository<CommonPost> commonPostRepository;

    private final CommentRepository commentRepository;

    private final ReportRepository reportRepository;

    @Transactional
    public CommonPostDto.CommonPostResponse savePostReport(ReportDto.ReportRequest reportDto) {
        // 신고유무 응답
        Long userId = reportDto.getUserId();
        Long postId = reportDto.getTargetId();

        System.out.println("postId = " + postId);
        log.debug("postId = {}", postId);
        // check
        verifyMember(userId);
        CommonPost commonPost = verifyPost(postId);

        // ReportRepository에 저장
        reportRepository.save(ReportDto.toEntity(reportDto));
        commonPost.plusPostReportCount();
        // PostRepository에 저장

        if (commonPost.getCountOfReports() >= 5)
            commonPostRepository.deleteById(postId);

        CommonPostDto dto = commonPost.toDto();

        CommonPostDto.CommonPostResponse responseDto = dto.toResponse();
        responseDto.setAlreadyReport(true);

        return responseDto;
    }

    public CommentDto.CommentResponse saveCommentReport(ReportDto.ReportRequest reportDto) {
        Long userId = reportDto.getUserId();
        Long commentId = reportDto.getTargetId();

        verifyMember(userId);
        Comment comment = verifyComment(commentId);

        reportRepository.save(ReportDto.toEntity(reportDto));
        comment.plusCommentReportCount();

        if (comment.getCountOfReports() >= 5)
            commentRepository.delete(comment);

        CommentDto dto = comment.toDto();
        CommentDto.CommentResponse responseDto = dto.toResponseDto();
        responseDto.setAlreadyReport(true);

        return responseDto;
    }

    private CommonPost verifyPost(Long postId) {
        return commonPostRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode._NOT_FOUND));
    }

    private Member verifyMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode._NOT_FOUND));
    }

    private Comment verifyComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode._NOT_FOUND));
    }

}
