package com.aptner.v3.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * The type Jwt util.
 *
 * @version jwt :0.12.3
 */
@Component
public class JwtUtil {

    /**
     * The constant AUTHORIZATION_HEADER.
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The constant BEARER_PREFIX.
     */
    public static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTHORITIES_KEY = "auth";

    private SecretKey secretKey;

    /**
     * Instantiates a new Jwt util.
     *
     * @param secret the secret
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * Gets expire time.
     *
     * @param expireTime the expire time
     * @return the expire time
     */
    public Date getExpireTime(Long expireTime) {
        return Date.from(Instant.now().plusMillis(expireTime));
    }

    /**
     * 만료 여부
     *
     * @param token the token
     * @return the boolean
     */
    public boolean isExpired(String token) {
        return parseClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Create token string.
     *
     * @param tokenType   the token type
     * @param username    the username
     * @param authorities the authorities
     * @param expireTime  the expire time
     * @return the string
     */
    public String createToken(String tokenType, String username, String authorities, Long expireTime) {

        Instant now = Instant.now();
        return Jwts.builder()
                .claim("tokenType", tokenType)
                .claim("username", username)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(getExpireTime(expireTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
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
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // @todo
            return e.getClaims();
        }
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

}
