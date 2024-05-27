package com.aptner.v3.global.util;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberDto;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.NOT_AVAILABLE_TOKEN;
import static com.aptner.v3.global.error.ErrorCode.TOKEN_CREATION_EXCEPTION;

/**
 * The type Jwt util.
 *
 * @version jwt :0.12.3
 */
@Slf4j
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
    private final long accessTokenExpirationInSeconds;
    private final long refreshTokenExpirationInSeconds;
    private final long refreshTokenCreateInHours;

    private SecretKey secretKey;

    /**
     * Instantiates a new Jwt util.
     *
     * @param secret                          the secret
     * @param accessTokenExpiration           the access token expiration
     * @param refreshTokenExpirationInSeconds the refresh token expiration in seconds
     * @param refreshTokenCreateInHours       the refresh token create in hours
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
     * Is 3 days left from expire boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean is3DaysLeftFromExpire(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            throw new AuthException(NOT_AVAILABLE_TOKEN);
        }

        Date expiration = claims.getExpiration();
        Instant threeDaysFromNow = Instant.now().plus(3, ChronoUnit.DAYS);
        return expiration.toInstant().isBefore(threeDaysFromNow);
    }

    /**
     * Is 3 days left from expire boolean.
     *
     * @param expireAt the expire at
     * @return the boolean
     */
    public boolean is3DaysLeftFromExpire(long expireAt) {
        long oneHourFromNow = Instant.now().plus(refreshTokenCreateInHours, ChronoUnit.HOURS).toEpochMilli();
        return expireAt <= oneHourFromNow;
    }

    /**
     * Create token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String createAccessToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("tokenType", "ACCESS")
                .setClaims(
                        memberToClaims(userDetails.getMember())
                )
                .claim(AUTHORITIES_KEY, userDetails.getAuthorities())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(getExpireTime(accessTokenExpirationInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Gets access token expiration in seconds.
     *
     * @return the access token expiration in seconds
     */
    public long getAccessTokenExpirationInSeconds() {
        return getExpireTime(accessTokenExpirationInSeconds).getTime();
    }


    /**
     * Create refresh token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String createRefreshToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("tokenType", "REFRESH")
                .setClaims(memberToClaims(userDetails.getMember()))
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(getExpireTime(refreshTokenExpirationInSeconds))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Gets refresh token expiration in seconds.
     *
     * @return the refresh token expiration in seconds
     */
    public long getRefreshTokenExpirationInSeconds() {
        return getExpireTime(refreshTokenExpirationInSeconds).getTime();
    }

    /**
     * Parse claims claims.
     *
     * @param token the token
     * @return the claims
     */
    public Claims parseClaims(String token) {

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
     * Member to claims map.
     *
     * @param member the member
     * @return the map
     */
    public Map<String, Object> memberToClaims(Member member) {

        List<SimpleGrantedAuthority> authority = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("username", member.getUsername());
        claims.put("roles", authority);

        return claims;
    }

    /**
     * Claims to member member.
     *
     * @param claims the claims
     * @return the member
     */
    public Member claimsToMember(Map<String, Object> claims) {
        Member member = new Member();
        member.setId(((Number) claims.get("id")).longValue());
        member.setUsername((String) claims.get("username"));
        // Add other member fields as needed
        List<MemberRole> roles = Arrays.stream((String[]) claims.get("roles"))
                .map(MemberRole::valueOf)
                .collect(Collectors.toList());
        member.setRoles(roles);
        return member;
    }

}
