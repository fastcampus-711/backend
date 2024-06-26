package com.aptner.v3.board.qna.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.board.qna.QnaStatus;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static com.aptner.v3.board.common_post.dto.CommonPostDto.CommonPostResponse.toMiddleSizeImageUrl;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class QnaDto extends CommonPostDto {
    private String type;
    private QnaStatus status = QnaStatus.AWAITING_RESPONSE;

    public static QnaDto of(BoardGroup boardGroup, MemberDto memberDto, QnaRequest request) {

        return QnaDto.builder()
                .id(request.getId())
                // member
                .memberDto(memberDto)
                // post
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                // qna
                .type(request.getType())
                .status(request.getStatus())
                // category
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public Qna toEntity(Member member, Category category) {
        return Qna.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.getImageUrls(),
                this.isVisible(),
                type,
                status
        );
    }

    @Override
    public QnaResponse toResponse() {

        QnaDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = QnaResponse.hasSecret(dto);

        return QnaResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : toMiddleSizeImageUrl(dto))
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())                                            // 조회수
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto()) // 공감
                .reactionType(isSecret || dto.getReactionType() == null ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())                      // 댓글 수
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // qna
                .status(dto.getStatus())
                .type(dto.getType())
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
    public QnaDto.QnaResponse toResponseWithComment() {

        QnaDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = QnaDto.QnaResponse.hasSecret(dto);

        return QnaDto.QnaResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : toMiddleSizeImageUrl(dto))
                .visible(dto.isVisible())
//                .comments(organizeChildComments(dto.getCommentDto()))
                // post info
                .hits(dto.getHits())                                            // 조회수
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto()) // 공감
                .reactionType(isSecret || dto.getReactionType() == null ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())                      // 댓글 수
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // qna
                .status(dto.getStatus())
                .type(dto.getType())
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
    public static class QnaRequest extends CommonPostDto.CommonPostRequest {
        private String type;
        private QnaStatus status = QnaStatus.AWAITING_RESPONSE;

        public static QnaDto.QnaRequest of(Long id, Long categoryId) {
            return QnaDto.QnaRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        @Override
        public QnaDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return QnaDto.of(
                    boardGroup,
                    user.toDto(),
                    (QnaDto.QnaRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class QnaResponse extends CommonPostDto.CommonPostResponse {

        private String type;
        private QnaStatus status;

    }
}
