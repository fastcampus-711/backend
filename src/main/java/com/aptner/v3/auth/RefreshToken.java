package com.aptner.v3.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name = "RefreshTokens")
public class RefreshToken {
    @Id
    @Column(name = "token_key")
    private String key;
    @Column(name = "token_value")
    private String value;

    protected RefreshToken() {
    }

    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }
}