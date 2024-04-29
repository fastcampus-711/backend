package com.aptner.v3.article;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "createdDt"),
        @Index(columnList = "createdBy")
})
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* 내용 */
    private String content;
    /* 생성일 */
    private LocalDateTime createdDt;
    /* 생성자 */
    private String createdBy;
    /* 수정일 */
    private LocalDateTime updatedDt;
    /* 수정자 */
    private String updatedBy;

    /* 게시글 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")  // 외래 키 열 이름
    private Article article;
}
