package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("QNAS")
public class Qna extends CommonPost {

    public enum Status {
        WAITING_FOR_ANSWER,
        ANSWERED,
        ACCEPTED
    }

    /* 상태값 */
    @Enumerated(EnumType.STRING)
    private Status status = Qna.Status.WAITING_FOR_ANSWER;
}
