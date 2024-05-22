package com.aptner.v3.global.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    @Column(updatable = false, columnDefinition = "DATETIME")
    private String createdAt;

    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;

    @JsonIgnore
    @Column(columnDefinition = "DATETIME")
    private String modifiedAt;

    @PrePersist
    public void onPrePersist() {
        // Insert 전에 호출
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = LocalDateTime.now().format(formatter);

        this.createdAt = LocalDateTime.parse(formattedNow, formatter).toString();
        this.modifiedAt = LocalDateTime.parse(formattedNow, formatter).toString();

        log.info("@@@@@@@@@time {}", LocalDateTime.parse(formattedNow, formatter));
    }
}
