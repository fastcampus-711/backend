package com.aptner.v3.global.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class BaseTimeDto {

    protected String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime createdAt;
    protected String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime modifiedAt;

    public BaseTimeDto(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @SuperBuilder
    public static class BaseResponse {
        protected String createdBy;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        protected LocalDateTime createdAt;

        protected String modifiedBy;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        protected LocalDateTime modifiedAt;
    }
}
