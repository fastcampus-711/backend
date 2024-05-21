package com.aptner.v3.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Entity
@Table(name = "RefreshTokens")
public class RefreshToken {
    @Id
    @Column(name = "token_key")
    private String key;

    @Setter
    @Column(name = "token_value")
    private String value;

    @Column(name = "expire_at")
    private long expireAt;

    protected RefreshToken() {
    }

    public RefreshToken(String key, String value, long expireAt) {
        this.key = key;
        this.value = value;
        this.expireAt = expireAt;
    }
}