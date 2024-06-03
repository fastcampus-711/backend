package com.aptner.v3.board.complain.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.complain.ComplainStatus;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static com.aptner.v3.board.common_post.dto.CommonPostCommentDto.organizeChildComments;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ComplainDto extends CommonPostDto {
    ComplainStatus status = ComplainStatus.RECEIVED;

    public static ComplainDto of(BoardGroup boardGroup, MemberDto memberDto, ComplainDto.ComplainRequest request) {

        return ComplainDto.builder()
                .id(request.getId())
                // member
                .memberDto(memberDto)
                // post
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                // complaint
                .status(request.getStatus())
                // category
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public Complain toEntity(Member member, Category category) {
        return Complain.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.getImageUrls(),
                this.isVisible(),
                status
        );
    }

    public ComplainDto.ComplainResponse toResponse() {

        ComplainDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return ComplainDto.ComplainResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(isSecret ? blindTitle : dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // complaint
                .status(dto.getStatus())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryName(dto.getCategoryDto().getName())
                // base
                .createdAt(createdAtFormat(dto))
                .createdBy(dto.getCreatedBy())
                .modifiedAt(modifiedAtFormat(dto))
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();

    }

    public ComplainDto.ComplainResponse toResponseWithComment() {

        ComplainDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return ComplainDto.ComplainResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(isSecret ? blindTitle : dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                .comments(organizeChildComments(dto.getCommentDto()))
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // complaint
                .status(dto.getStatus())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryName(dto.getCategoryDto().getName())
                // base
                .createdAt(createdAtFormat(dto))
                .createdBy(dto.getCreatedBy())
                .modifiedAt(modifiedAtFormat(dto))
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
    public static class ComplainRequest extends CommonPostDto.CommonPostRequest {
        private ComplainStatus status = ComplainStatus.RECEIVED;

        public static ComplainDto.ComplainRequest of(Long id, Long categoryId) {
            return ComplainDto.ComplainRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        @Override
        public ComplainDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return ComplainDto.of(
                    boardGroup,
                    user.toDto(),
                    (ComplainDto.ComplainRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class ComplainResponse extends CommonPostDto.CommonPostResponse {
        private ComplainStatus status;
    }
}
