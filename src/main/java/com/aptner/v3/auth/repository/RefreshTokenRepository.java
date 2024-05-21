package com.aptner.v3.auth.repository;

import com.aptner.v3.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByKey(String key);

    boolean existsByKey(String key);

    void deleteByKey(String key);
}
