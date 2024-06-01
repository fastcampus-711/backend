package com.aptner.v3.board.qna;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QnaStatus implements Status {

    AWAITING_RESPONSE("AWAITING_RESPONSE", "답변대기"),
    RESPONSE_ACCEPTED("RESPONSE_ACCEPTED", "답변채택");

    private final String code;
    private final String description;
}
