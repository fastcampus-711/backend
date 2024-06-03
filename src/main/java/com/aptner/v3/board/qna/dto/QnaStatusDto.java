package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.qna.QnaStatus;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class QnaStatusDto {

    Long postId;
    Long commentId;
    QnaStatus status;

    BoardGroup boardGroup;
    MemberDto memberDto;

    public static QnaStatusDto of(BoardGroup boardGroup, MemberDto memberDto, QnaStatusRequest request) {
        return QnaStatusDto.builder()
                // post
                .postId(request.getPostId())
                // comment
                .commentId(request.getCommentId())
                // member
                .memberDto(memberDto)
                // qna
                .status(request.getStatus())
                .build();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QnaStatusRequest {
        @NotNull
        Long postId;
        @NotNull
        Long commentId;
        @NotBlank
        QnaStatus status;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QnaStatusResponse {
        private String code;
        private String description;

        public static QnaStatusResponse from(QnaStatus status) {
            return new QnaStatusResponse(status.name(), status.getDescription());
        }

        public static Object toList() {
            return Arrays.stream(QnaStatus.values())
                    .map(QnaStatusResponse::from)
                    .collect(Collectors.toList());
        }

    }
}
