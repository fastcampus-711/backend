package com.aptner.v3.board.complain;

import com.aptner.v3.board.qna.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ComplainStatus implements Status {
    RECEIVED("RECEIVED","접수"),
    IN_PROGRESS("IN_PROGRESS","처리중"),
    COMPLETED("COMPLETED", "처리완료")
    ;

    private final String code;
    private final String description;
}
