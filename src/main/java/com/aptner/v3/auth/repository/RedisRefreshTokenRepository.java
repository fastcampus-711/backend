package com.aptner.v3.auth.repository;

import com.aptner.v3.auth.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@Repository
@Profile("notyet")
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    @Value("${jwt.refresh-token-saved-in-seconds:259200}")  // 3일
    private static long EXPIRE_TIME;
    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        valueOperations.set(
                refreshToken.getKey(),
                refreshToken,
                EXPIRE_TIME,
                TimeUnit.SECONDS
        );
        return refreshToken;
    }

    @Override
    public RefreshToken findByKey(String key) {
        Object value = valueOperations.get(key);
        if (value instanceof RefreshToken) {
            return (RefreshToken) value;
        }
        return null;
    }

    @Override
    public boolean existsByKey(String key) {
        return valueOperations.getOperations().hasKey(key).booleanValue();
    }

    @Override
    public void deleteByKey(String key) {
        valueOperations.getOperations().delete(key);
    }

}
