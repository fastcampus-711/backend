package com.aptner.v3.board.notices.domain;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("NoticePost")
public class NoticePost extends CommonPost {

}