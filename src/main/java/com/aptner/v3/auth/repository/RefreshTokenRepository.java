package com.aptner.v3.auth.repository;

import com.aptner.v3.auth.RefreshToken;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    RefreshToken findByKey(String key);

    boolean existsByKey(String key);

    void deleteByKey(String key);
}
