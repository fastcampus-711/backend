package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.aptner.v3.global.util.MemberUtil;
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
                .boardGroup(boardGroup)
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .createdBy(null)
                .createdAt(null)
                .modifiedAt(null)
                .modifiedBy(null)
                .build();
    }

    public static NoticePostDto from(FreePost entity) {

        return NoticePostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(MemberUtil.getMemberId() != entity.getMember().getId())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public NoticePost toEntity(Member member, Category category) {
        return NoticePost.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.isVisible(),
                postAt
        );
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

        public NoticePostDto createDto(BoardGroup boardGroup, CustomUserDetails user, NoticePostDto.NoticeRequest request) {
            return NoticePostDto.of(
                    boardGroup,
                    user.toDto(),
                    request
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
