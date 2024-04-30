package com.aptner.v3.article;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ArticleCommentDTO {
    private Long id;  // 댓글 ID
    private String content;  // 내용
    private LocalDateTime createdAt;  // 생성일
    private String createdBy;  // 생성자
    private LocalDateTime updatedAt;  // 수정일
    private String updatedBy;  // 수정자
}
