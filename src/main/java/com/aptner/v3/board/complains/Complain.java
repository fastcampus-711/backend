package com.aptner.v3.board.complains;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("COMPLAIN")
public class Complain extends CommonPost {

    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private Status status = Complain.Status.RECEIVED;
}
