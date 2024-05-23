package com.aptner.v3.board.frees.domain;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("FreePost")
public class FreePost extends CommonPost {

}
