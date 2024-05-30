package com.aptner.v3.auth.service;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aptner.v3.global.error.ErrorCode.NOT_AVAILABLE_TOKEN;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void logout(String username) {
        refreshTokenRepository.deleteByKey(username);
    }

    @Transactional
    public TokenDto reissue(String accessToken) {

        UsernamePasswordAuthenticationToken token = getUsernamePasswordAuthenticationToken(accessToken);
        Authentication authentication = attemptAuthentication(token); // DB 조회
        log.debug("authentication : {}", authentication);
        if (authentication == null) {
            return null;
        }
        // 5. 새로운 토큰 생성
        return successfulAuthentication(authentication);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String accessToken) {

        try {
            // 1. Access Token Parsing 유효 확인
            Claims claims = jwtUtil.parseClaims(accessToken);
            if (claims == null) {
                throw new AuthException(NOT_AVAILABLE_TOKEN);
            }

            // 2. Token 에서 Member 객체 생성
            Member member = jwtUtil.claimsToMember(claims);

            // 3. Refresh Token Parsing 유효 확인
            RefreshToken refreshToken = getRefreshToken(member.getUsername());
            Claims refreshTokenClaims = jwtUtil.parseClaims(refreshToken.getValue());
            if (refreshTokenClaims != null) {
                throw new AuthException(NOT_AVAILABLE_TOKEN);
            }

            // 4. Refresh Token 만료 여부
            if (jwtUtil.isExpired(refreshToken.getValue())) {
                throw new AuthException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }

            // 5. 새로운 Access Token 생성
            CustomUserDetails userDetails = new CustomUserDetails(member);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(NOT_AVAILABLE_TOKEN);
        }
    }

    @Transactional
    public TokenDto login(LoginDto request) {

        Authentication authentication = attemptAuthentication(request.toAuthentication());  // DB 조회
        log.debug("authentication : {}", authentication);
        if (authentication == null) {
            return null;
        }
        return successfulAuthentication(authentication);
    }

    private Authentication attemptAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
        // set Authentication
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private TokenDto successfulAuthentication(Authentication authentication) {

        return TokenDto.builder()
                .accessToken(jwtUtil.createAccessToken(authentication))
                .accessTokenExpiresIn(jwtUtil.getAccessTokenExpirationInSeconds())
                .refreshToken(getOrCreateRefreshToken(authentication))
                .build();
    }

    private String getOrCreateRefreshToken(Authentication authentication) {

        RefreshToken refreshToken = getRefreshToken(authentication.getName());
        if (refreshToken == null || jwtUtil.is3DaysLeftFromExpire(refreshToken.getValue())) {
            return reCreateAndSaveRefreshToken(authentication);
        } else {
            // Otherwise, use the existing refresh token
            return refreshToken.getValue();
        }
    }

    private String reCreateAndSaveRefreshToken(Authentication authentication) {
        String newRefreshToken = jwtUtil.createRefreshToken(authentication);
        refreshTokenRepository.save(new RefreshToken(
                authentication.getName(),
                newRefreshToken,
                jwtUtil.getRefreshTokenExpirationInSeconds()
        ));
        return newRefreshToken;
    }

    private RefreshToken getRefreshToken(String username) {
        return refreshTokenRepository.findByKey(username).orElse(null);
    }

}
