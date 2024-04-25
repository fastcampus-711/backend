package com.aptner.v3.article;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id;
    /* 내용 */
    private String content;
    /* 생성일 */
    private LocalDateTime createdAt;
    /* 생성자 */
    private String createdBy;
    /* 수정일 */
    private LocalDateTime updatedAt;
    /* 수정자 */
    private String updatedBy;

    /* 게시글 */
    private Article article;
}
