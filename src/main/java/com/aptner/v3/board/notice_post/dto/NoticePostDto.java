package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class NoticePostDto extends CommonPostDto {
    private LocalDateTime postAt;
    public static NoticePostDto of(BoardGroup boardGroup, MemberDto memberDto, NoticePostDto.NoticeRequest request) {

        return NoticePostDto.builder()
                .id(request.getId())
                .memberDto(memberDto)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .hits(null)
                .reactionColumnsDto(null)
                .countOfComments(null)
                .visible(request.isVisible())
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .createdBy(null)
                .createdAt(null)
                .modifiedAt(null)
                .modifiedBy(null)
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

        return NoticePostDto.NoticeResponse.builder()
                .id(dto.getId())
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                .visible(dto.isVisible())
                .title(isSecret ? blindTitle : dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(dto.getImageUrls())
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .countOfComments(dto.getCountOfComments())
                .boardGroup(dto.getBoardGroup())
                .categoryName(dto.getCategoryDto().getName())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                .build();
    }

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class NoticeRequest extends CommonPostDto.CommonPostRequest {
        private LocalDateTime postAt; // 언제 부터 노출 가능한 시간.

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
        private LocalDateTime postAt;

        public static NoticePostDto.NoticeResponse from(FreePostDto dto) {

            String blindTitle = "비밀 게시글입니다.";
            String blindContent = "비밀 게시글입니다.";
            boolean isSecret = hasSecret(dto);

            return NoticePostDto.NoticeResponse.builder()
                    .id(dto.getId())
                    .userId(dto.getMemberDto().getId())
                    .userNickname(dto.getMemberDto().getNickname())
                    .userImage(dto.getMemberDto().getImage())
                    .visible(dto.isVisible())
                    .title(isSecret ? blindTitle : dto.getTitle())
                    .content(isSecret ? blindContent : dto.getContent())
                    .hits(dto.getHits())
                    .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                    .countOfComments(dto.getCountOfComments())
                    .boardGroup(dto.getBoardGroup())
                    .categoryName(dto.getCategoryDto().getName())
                    .createdAt(dto.getCreatedAt())
                    .createdBy(dto.getCreatedBy())
                    .modifiedAt(dto.getModifiedAt())
                    .modifiedBy(dto.getModifiedBy())
                    .build();

        }
    }
}
