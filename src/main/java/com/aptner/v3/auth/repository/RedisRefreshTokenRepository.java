package com.aptner.v3.auth.repository;

import com.aptner.v3.auth.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Repository
@Primary
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        long ttl = Duration.between(
                Instant.now(), Instant.ofEpochMilli(refreshToken.getExpireAt())
        ).getSeconds();
        log.debug("duration ttl {}", Duration.ofSeconds(ttl));
        valueOperations.set(
                refreshToken.getKey(),
                refreshToken,
                Duration.ofSeconds(ttl)
        );
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByKey(String key) {
        Object value = valueOperations.get(key);
        if (value instanceof RefreshToken) {
            RefreshToken refreshToken = (RefreshToken) value;
            if (refreshToken.getExpireAt() > Instant.now().toEpochMilli()) {
                return Optional.of(refreshToken);
            } else {
                deleteByKey(key); // Remove expired token
            }
        }
        return Optional.empty();
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
