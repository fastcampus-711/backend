package com.aptner.v3.article;

import com.aptner.v3.menu.MenuItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true, name = "parent_id")
    @JsonBackReference  // 부모에 대한 순환 참조를 방지
    private ArticleComment parent;

    /* 내용 */
    @Column(columnDefinition = "TEXT")
    private String content;
    /* 생성일 */
    @CreatedDate
    private LocalDateTime createdAt;
    /* 생성자 */
    @CreatedBy
    private String createdBy;
    /* 수정일 */
    @LastModifiedDate
    private LocalDateTime updatedAt;
    /* 수정자 */
    @LastModifiedBy
    private String updatedBy;

    /* 게시글 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")  // 외래 키 열 이름
    private Article article;

    private List<ArticleComment> items;
}
