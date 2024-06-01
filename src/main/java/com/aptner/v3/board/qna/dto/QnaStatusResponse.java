package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.qna.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class QnaStatusResponse {
    private String code;
    private String description;

    public static QnaStatusResponse from(QnaStatus status) {
        return new QnaStatusResponse(status.name(), status.getDescription());
    }

    public static Object toList() {

        return Arrays.stream(QnaStatus.values())
                .map(QnaStatusResponse::from)
//                .collect(Collectors.toMap(QnaStatusResponse::getCode, Function.identity()));
                .collect(Collectors.toList());
    }

}