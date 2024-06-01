package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.service.CountCommentsAndReactionApplyService;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.aptner.v3.global.error.ErrorCode.INVALID_REQUEST;

@Slf4j
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;
    private final MemberRepository memberRepository;
    private final CountCommentsAndReactionApplyService<Comment> countOfReactionAndCommentApplyService;

    public CommentService(CommentRepository commentRepository,
                          CommonPostRepository<CommonPost> commonPostRepository,
                          MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.commonPostRepository = commonPostRepository;
        this.countOfReactionAndCommentApplyService = new CountCommentsAndReactionApplyService<>(commentRepository);
        this.memberRepository = memberRepository;
    }

    public CommentDto addComment(CommentDto dto) {

        Member member = verifyMember(dto);
        CommonPost commonPost = verifyPost(dto);
        Comment comment = dto.toEntity(commonPost, member);

        if (dto.getParentCommentId() != 0) {
            log.debug("dto.getParentCommentId()!=null : {}", dto.getParentCommentId());
            Comment parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(InvalidTableIdException::new);
            log.debug("dto.getParentCommentId()!=null : {}", parentComment);
            parentComment.addChildComment(comment);
        } else {
            comment = commentRepository.save(comment);
        }
        // 댓글 수
        commonPost.setCountOfComments(commonPost.getCountOfComments() + 1);
        return comment.toDto();
    }

    public CommentDto updateComment(CommentDto dto) {

        Member member = verifyMember(dto);
        CommonPost commonPost = verifyPost(dto);
        Comment comment = verifyComment(dto);

        // 내용 수정
        if (dto.getContent() != null) {
            comment.setContent(dto.getContent());
        }
        comment.setVisible(dto.isVisible());

        // 저장
        commentRepository.flush();
        return comment.toDto();
    }

    public long deleteComment(CommentDto dto) {
        verifyMember(dto);
        CommonPost commonPost = verifyPost(dto);
        Comment comment = verifyComment(dto);
        // 댓글 수
        commonPost.setCountOfComments(commonPost.getCountOfComments() - 1);
        commentRepository.deleteById(comment.getId());
        return comment.getId();
    }

    private Member verifyMember(CommentDto dto) {
        // id check
        if (dto.getMemberDto().getId() == null) {
            log.error("MEMBER ID 없음");
            throw new UserException(INVALID_REQUEST);
        }
        log.debug("verifyMember : {}", dto.getMemberDto().getId());
        return memberRepository.findById(dto.getMemberDto().getId())
                .orElseThrow(InvalidTableIdException::new);
    }

    private CommonPost verifyPost(CommentDto dto) {
        // id check
        if (dto.getPostId() == null) {
            log.error("POST ID 없음");
            throw new PostException(INVALID_REQUEST);
        }
        log.debug("verifyPost : {}", dto.getPostId());
        CommonPost commonPost = commonPostRepository.findById(dto.getPostId())
                .orElseThrow(InvalidTableIdException::new);
        return commonPost;
    }

    private Comment verifyComment(CommentDto dto) {
        // id check
        if (dto.getCommentId() == null) {
            log.error("COMMENT ID 없음");
            throw new PostException(INVALID_REQUEST);
        }

        log.debug("verifyComment : {}", dto.getCommentId());
        // exists
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(InvalidTableIdException::new);

        // 자신이 작성한 글이 아닌 경우
        if (!comment.getMember().getId().equals(dto.getMemberDto().getId())) {
            log.error("COMMENT 저장에 MEMBER ID 가 로그인 유저와 상이함");
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }
        return comment;
    }

}
