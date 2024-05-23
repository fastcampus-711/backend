package com.aptner.v3.board.complains;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("ComplainPost")
public class ComplainPost extends CommonPost {

    public enum Status {
        RECEIVED,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private Status status = ComplainPost.Status.RECEIVED;
}
