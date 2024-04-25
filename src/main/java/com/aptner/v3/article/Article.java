package com.aptner.v3.article;

import lombok.Getter;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@ToString
@Table()
public class Article {
    private Long id;
    /* 제목 */
    private String title;
    /* 내용 */
    private String content;
    /* 태그 */
    private String hashtag;

    /* 생성일 */
    private LocalDateTime createdAt;
    /* 생성자 */
    private String createdBy;
    /* 수정일 */
    private LocalDateTime updatedAt;
    /* 수정자 */
    private String updatedBy;
    /* 삭제일 */
    private LocalDateTime deletedAt;
}
