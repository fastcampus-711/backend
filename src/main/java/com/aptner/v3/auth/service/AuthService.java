package com.aptner.v3.auth.service;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.exception.AttachException;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.aptner.v3.global.error.ErrorCode.NOT_AVAILABLE_TOKEN;
import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;

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

        // 1. Access Token 검증
        if (!jwtUtil.validateToken(accessToken)) {
            throw new AuthException(NOT_AVAILABLE_TOKEN);
        }

        // 2. Access Token 에서 Member ID 찾기
        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        String username = authentication.getName();

        // 3. Refresh Token 찾기
        log.debug("{}", authentication);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(username)
                .orElseThrow(() -> new UserException(_NOT_FOUND));

        if(!jwtUtil.validateToken(refreshToken.getValue())) {
            throw new AttachException(NOT_AVAILABLE_TOKEN);
        }

        // 5. 새로운 토큰 생성
        return successfulAuthentication(authentication, refreshToken);
    }

    @Transactional
    public TokenDto login(LoginDto request) {

//        try {
            Authentication authentication = attemptAuthentication(request);
            if (authentication == null) {
                return null;
            }
            log.debug("authentication : {}", authentication);
            return successfulAuthentication(authentication, null);
//        } catch (AuthenticationException e) {
//            throw e;
//        }
    }

    private Authentication attemptAuthentication(LoginDto request) {

        // 1. Login ID/PW , AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        // 아파트너사 API
        // userDB를 디비정보를 넣는다.
        // 2. 실제로 사용자 비밀번호 체크
        // MemberService.loadUserByUsername (UserDetailService) 실행됨
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private TokenDto successfulAuthentication(Authentication authentication, RefreshToken originRefreshToken) {
        // 3. 토큰 생성
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = jwtUtil.createAccessToken(username, authorities);
        String refreshToken = determineRefreshToken(originRefreshToken, username, authorities);

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(jwtUtil.getAccessTokenExpirationInSeconds()) // @todo
                .refreshToken(refreshToken)
                .build();
    }

    private String determineRefreshToken(RefreshToken originRefreshToken, String username, String authorities) {

        if (originRefreshToken == null || jwtUtil.is3DaysLeftFromExpire(originRefreshToken)) {
            // If originRefreshToken is null or its expiration time is within 1 hour, create a new refresh token
            String newRefreshToken = jwtUtil.createRefreshToken(username, authorities);
            refreshTokenRepository.save(new RefreshToken(username, newRefreshToken, jwtUtil.getRefreshTokenExpirationInSeconds()));
            return newRefreshToken;
        } else {
            // Otherwise, use the existing refresh token
            return originRefreshToken.getValue();
        }
    }

}
