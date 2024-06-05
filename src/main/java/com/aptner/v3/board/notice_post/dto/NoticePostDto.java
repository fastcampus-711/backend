package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.aptner.v3.board.common_post.dto.CommonPostCommentDto.organizeChildComments;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class NoticePostDto extends CommonPostDto {
    private boolean isImport;
    private boolean isDuty;
    private LocalDateTime scheduleStartAt = LocalDateTime.now();
    private LocalDateTime scheduleEndAt = LocalDateTime.now().plusDays(1);
    private LocalDateTime postAt = LocalDateTime.now();
    public static NoticePostDto of(BoardGroup boardGroup, MemberDto memberDto, NoticePostDto.NoticeRequest request) {

        return NoticePostDto.builder()
                .id(request.getId())
                // member
                .memberDto(memberDto)
                // post
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                // notice
                .isImport(request.isImport())
                .isDuty(request.isDuty())
                .scheduleStartAt(request.getScheduleStartAt())
                .scheduleEndAt(request.getScheduleEndAt())
                .postAt(request.getPostAt())
                //category
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public NoticePost toEntity(Member member, Category category) {
        return NoticePost.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.getImageUrls(),
                this.isVisible(),
                postAt
        );
    }

    @Override
    public NoticePostDto.NoticeResponse toResponse() {

        NoticePostDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return NoticeResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // notice
                .isImport(dto.isImport())
                .isDuty(dto.isDuty())
                .scheduleStartAt(dto.getScheduleStartAt())
                .scheduleEndAt(dto.getScheduleEndAt())
                .postAt(dto.getPostAt())
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();
    }

    @Override
    public NoticePostDto.NoticeResponse toResponseWithComment() {

        NoticePostDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return NoticeResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                .comments(organizeChildComments(dto.getCommentDto()))
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // notice
                .postAt(dto.getPostAt())
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();
    }

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class NoticeRequest extends CommonPostDto.CommonPostRequest {
        private boolean isImport;
        private boolean isDuty;
        private LocalDateTime scheduleStartAt;
        private LocalDateTime scheduleEndAt;
        private LocalDateTime postAt;

        public static NoticePostDto.NoticeRequest of(Long id, Long categoryId) {
            return NoticePostDto.NoticeRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        public NoticePostDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return NoticePostDto.of(
                    boardGroup,
                    user.toDto(),
                    (NoticePostDto.NoticeRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class NoticeResponse extends CommonPostDto.CommonPostResponse {
        private boolean isImport;
        private boolean isDuty;
        private LocalDateTime scheduleStartAt;
        private LocalDateTime scheduleEndAt;
        private LocalDateTime postAt;

    }
}
