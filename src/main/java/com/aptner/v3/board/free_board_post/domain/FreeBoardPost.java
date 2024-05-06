package com.aptner.v3.board.free_board_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class FreeBoardPost extends CommonPost {
    private LocalDateTime blindAt;
    private String blindBy;
}
