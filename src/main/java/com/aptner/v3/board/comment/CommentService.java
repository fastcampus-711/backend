package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CommentException;
import com.aptner.v3.global.exception.PostException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import com.aptner.v3.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.INVALID_REQUEST;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

@Slf4j
@Service
@Transactional
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository<CommentReaction> commentReactionRepository;

    public CommentService(CommentRepository commentRepository,
                          CommonPostRepository<CommonPost> commonPostRepository,
                          MemberRepository memberRepository, ReactionRepository<CommentReaction> commentReactionRepository) {
        this.commentRepository = commentRepository;
        this.commonPostRepository = commonPostRepository;
        this.memberRepository = memberRepository;
        this.commentReactionRepository = commentReactionRepository;
    }

    public CommentDto addComment(CommentDto dto) {

        Member member = verifyMember(dto);
        CommonPost commonPost = verifyPost(dto);
        Comment comment = dto.toEntity(commonPost, member);

        if (dto.getParentCommentId() != null && dto.getParentCommentId() != 0) {
            log.debug("dto.getParentCommentId()!=null : {}", dto.getParentCommentId());
            Comment parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new CommentException(_NOT_FOUND));
            log.debug("dto.getParentCommentId()!=null : {}", parentComment);
            log.debug("dto.getParentCommentId()!=null comment : {}", comment);

            if (parentComment.getParentCommentId() != null && parentComment.getParentCommentId() != 0) {
                // 2-depth 이상 제외
                log.error("dto.getParentCommentId()!=null comment : {}", comment);
                throw new PostException(ErrorCode.COMMENT_DEPTH_IS_OVER);
            }
            parentComment.addChildComment(comment);
        }

        comment = commentRepository.save(comment);

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

    public Page<CommentDto> getPostWithComment(MemberDto memberDto, long postId, Pageable pageable) {
        // check
        CommonPost post = commonPostRepository.findById(postId).orElseThrow(() -> new PostException(_NOT_FOUND));

        // get
        Page<CommentDto> commentDtosPage = Page.empty(pageable);
        try {
            Page<Comment> commentsPage = commentRepository.findAllByPostIdAndParentCommentIdIsNull(postId, pageable);

            // reaction
            Map<Long, ReactionType> mapCommentIdAndReactionType = commentReactionRepository.findByDtypeAndTargetId("CommentReaction", postId)
                    .orElse(Collections.emptyList())
                    .stream()
                    .collect(Collectors.toMap(
                            CommentReaction::getTargetId,
                            CommentReaction::getReactionType,
                            (existingValue, newValue) -> newValue // Choose how to handle duplicates
                    ));

            // Ensure the map contains a default entry
            mapCommentIdAndReactionType.putIfAbsent(-1L, ReactionType.DEFAULT);

            commentDtosPage = commentsPage.map(comment -> convertToDto(comment, mapCommentIdAndReactionType));
        } catch (EntityNotFoundException e) {
            log.warn("Entity not found: {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("IllegalStateException: {}", e.getMessage());
        }
        return commentDtosPage;
    }

    private CommentDto convertToDto(Comment comment, Map<Long, ReactionType> mapCommentIdAndReactionType) {
        log.debug("comment : {}", comment);
        CommentDto dto = comment.toDto();
        dto.setReactionType(mapCommentIdAndReactionType.getOrDefault(comment.getId(), ReactionType.DEFAULT));
        return dto;
    }

    private Member verifyMember(CommentDto dto) {
        // id check
        if (dto.getMemberDto().getId() == null) {
            log.error("MEMBER ID 없음");
            throw new UserException(INVALID_REQUEST);
        }
        log.debug("verifyMember : {}", dto.getMemberDto().getId());
        return memberRepository.findById(dto.getMemberDto().getId())
                .orElseThrow(() -> new UserException(_NOT_FOUND));
    }

    private CommonPost verifyPost(CommentDto dto) {
        // id check
        if (dto.getPostId() == null) {
            log.error("POST ID 없음");
            throw new PostException(INVALID_REQUEST);
        }
        log.debug("verifyPost : {}", dto.getPostId());
        CommonPost commonPost = commonPostRepository.findById(dto.getPostId())
                .orElseThrow(() -> new PostException(_NOT_FOUND));
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
                .orElseThrow(() -> new CommentException(_NOT_FOUND));

        // 자신이 작성한 글이 아닌 경우
        if (!comment.getMember().getId().equals(dto.getMemberDto().getId())) {
            log.error("COMMENT 저장에 MEMBER ID 가 로그인 유저와 상이함");
            throw new PostException(ErrorCode.INSUFFICIENT_AUTHORITY);
        }
        return comment;
    }

}
