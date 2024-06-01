package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.qna.QnaStatus;
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
    QnaStatus status;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QnaStatusRequest {
        Long postId;
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
