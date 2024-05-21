package com.aptner.v3.global.util;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.global.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.TOKEN_CREATION_EXCEPTION;

/**
 * The type Jwt util.
 *
 * @version jwt :0.12.3
 */
@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenExpirationInSeconds;
    private final long refreshTokenExpirationInSeconds;
    private final long refreshTokenCreateInHours;

    private SecretKey secretKey;

    /**
     * Instantiates a new Jwt util.
     *
     * @param secret the secret
     */
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expire-in-seconds}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expire-in-seconds}") long refreshTokenExpirationInSeconds,
            @Value("${jwt.refresh-create-in-hours}") long refreshTokenCreateInHours
    ) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
        this.accessTokenExpirationInSeconds = accessTokenExpiration;
        this.refreshTokenExpirationInSeconds = refreshTokenExpirationInSeconds;
        this.refreshTokenCreateInHours = refreshTokenCreateInHours;
    }

    /**
     * Gets expire time.
     *
     * @param expireTime the expire time
     * @return the expire time
     */
    public Date getExpireTime(Long expireTime) {
        return Date.from(Instant.now().plusSeconds(expireTime));
    }

    /**
     * 만료 여부
     *
     * @param token the token
     * @return the boolean
     */
    public boolean isExpired(String token) {
        return Objects.requireNonNull(parseClaims(token))
                .getExpiration()
                .before(new Date());
    }

    /**
     * 유효 토큰 검증
     *
     * @param token token
     * @return the boolean
     */
    public boolean validateToken(String token) {
        return parseClaims(token) != null;
    }

    /**
     * Create token string.
     *
     * @param username    the username
     * @param authorities the authorities
     * @return the string
     */
    public String createAccessToken(String username, String authorities) {

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "ACCESS")
                .claim("username", username)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(getExpireTime(accessTokenExpirationInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public long getAccessTokenExpirationInSeconds() {
        return getExpireTime(accessTokenExpirationInSeconds).getTime();
    }

    /**
     * Create token string.
     *
     * @param username    the username
     * @param authorities the authorities
     * @return the string
     */
    public String createRefreshToken(String username, String authorities) {

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "REFRESH")
                .claim("username", username)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(getExpireTime(refreshTokenExpirationInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public long getRefreshTokenExpirationInSeconds() {
        return getExpireTime(refreshTokenExpirationInSeconds).getTime();
    }

    /**
     * Gets authentication.
     *
     * @param token the token
     * @return the authentication
     */
    public Authentication getAuthentication(String token) {

        try {
            // 유저 정보
            Claims claims = parseClaims(token);
            assert claims != null;
            String username = getUsername(claims);

            // Authentication 객체
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(getAuthority(claims))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (Exception e) {
            throw new MalformedJwtException("정보가 올바르지 않은 토큰입니다.");
        }
    }

    private Claims parseClaims(String token) {

        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException | DecodingException e) {
            log.debug(e.getMessage());
            throw new AuthException(TOKEN_CREATION_EXCEPTION, e.getMessage());
        }
        return null;
    }

    /**
     * username 추출
     */
    private String getUsername(Claims claims) {
        return claims.getSubject();
    }

    /**
     * Authority 추출
     */
    private String getRole(Claims claims) {
        return claims.get(AUTHORITIES_KEY, String.class);
    }

    private String[] getAuthority(Claims claims) {
        return claims.get(AUTHORITIES_KEY).toString().split(",");
    }

    public boolean is3DaysLeftFromExpire(RefreshToken originRefreshToken) {
        long oneHourFromNow = Instant.now().plus(refreshTokenCreateInHours, ChronoUnit.HOURS).toEpochMilli();
        return originRefreshToken.getExpireAt() <= oneHourFromNow;
    }
}
