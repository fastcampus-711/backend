package com.aptner.v3.board.qna.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.board.qna.QnaStatus;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class QnaDto extends CommonPostDto {
    private String type;
    private QnaStatus status;

    public static QnaDto of(BoardGroup boardGroup, MemberDto memberDto, QnaRequest request) {

        log.debug("qna of");
        return QnaDto.builder()
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

    public Qna toEntity(Member member, Category category) {
        return Qna.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.isVisible(),
                type,
                status
        );
    }

    public QnaDto.QnaResponse toResponse() {

        QnaDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = QnaResponse.hasSecret(dto);

        return QnaResponse.builder()
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

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class QnaRequest extends CommonPostDto.CommonPostRequest {
        private String type;
        private QnaStatus status;

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
