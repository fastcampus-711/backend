package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.repository.CategoryRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common_post.PostSpecification;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.Status;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Qualifier("noticePostService")
public class NoticePostService extends CommonPostService<NoticePost, NoticePostDto, NoticePostDto.NoticeRequest, NoticePostDto.NoticeResponse> {

    private final NoticePostRepository noticePostRepository;

    public NoticePostService(MemberRepository memberRepository,
                             CategoryRepository categoryRepository,
                             @Qualifier("noticePostRepository") NoticePostRepository noticePostRepository,
                             ReactionRepository<PostReaction> postReactionRepository
    ) {
        super(memberRepository, categoryRepository, noticePostRepository, postReactionRepository);
        this.noticePostRepository = noticePostRepository;
    }

    public List<NoticePost> getNoticePostsByScheduleRange(LocalDate start, LocalDate end, BoardGroup boardGroup) {
        LocalDateTime startOfDay  = start.atStartOfDay();
        LocalDateTime endOfNextDay  = end.plusDays(1).atStartOfDay();

        return noticePostRepository.findByScheduleRangeAndDtype(startOfDay , endOfNextDay, boardGroup.getTable());
    }

    public Page<NoticePostDto> getPostList(BoardGroup boardGroup, Long categoryId, String keyword, Status status, Long userId, boolean isDuty, Pageable pageable) {

        Specification<NoticePost> spec = Specification
                .where(PostSpecification.<NoticePost>hasBoardGroup(boardGroup))
                .and(PostSpecification.hasCategoryId(categoryId))
                .and(PostSpecification.hasKeyword(keyword))
                .and(PostSpecification.hasStatus(status))
                .and(PostSpecification.hasAuthor(userId))
                .and(PostSpecification.isDuty(isDuty))
                ;

        Page<NoticePost> posts = noticePostRepository.findAll(spec, pageable);
        return posts.map(e -> (NoticePostDto) e.toDto());
    }
}
