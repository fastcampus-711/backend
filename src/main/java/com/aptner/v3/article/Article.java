package com.aptner.v3.article;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdDt"),
        @Index(columnList = "createdBy")
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* 제목 */
    @Setter private String title;
    /* 내용 */
    @Setter private String content;
    /* 태그 */
    @Setter private String hashtag;

    /* 생성일 */
    @CreatedDate
    private LocalDateTime createdDt;
    /* 생성자 */
    @CreatedBy
    private String createdBy;
    /* 수정일 */
    @LastModifiedDate
    private LocalDateTime updatedDt;
    /* 수정자 */
    @LastModifiedBy
    private String updatedBy;

}
