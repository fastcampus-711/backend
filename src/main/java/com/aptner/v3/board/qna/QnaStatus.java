package com.aptner.v3.board.qna;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QnaStatus {

    AWAITING_RESPONSE("답변대기"),
    RESPONSE_ACCEPTED("답변채택")
    ;

    private final String description;
}
