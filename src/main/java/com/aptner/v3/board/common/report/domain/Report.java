package com.aptner.v3.board.common.report.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    private Long userId;

    @Setter
    private long targetId;

    @Setter
    @Enumerated(EnumType.STRING)
    private ReportColumns reportColumns;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /*@Column(updatable = false, insertable = false)
    private String dtype;*/
}
