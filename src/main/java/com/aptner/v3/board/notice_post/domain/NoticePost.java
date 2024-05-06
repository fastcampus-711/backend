package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class NoticePost extends CommonPost {
    private LocalDateTime postAt;
}
