package com.aptner.v3.global.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseTimeEntity {

    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat. ISO.DATE_TIME)
    @JsonIgnore
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat. ISO.DATE_TIME)
    @JsonIgnore
    @Column(updatable = false)
    private LocalDateTime modifiedAt;

}
