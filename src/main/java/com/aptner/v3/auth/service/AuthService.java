package com.aptner.v3.auth.service;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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
    public TokenDto login(LoginDto request) {

        try {
            Authentication authentication = attemptAuthentication(request);
            log.info("authentication : {}", authentication);
            if (authentication == null) {
                log.info("없다?");
                return null;
            }
            return successfulAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    private Authentication attemptAuthentication(LoginDto request) {

        // 1. Login ID/PW , AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();
        log.info("attempAuthentication {}", authenticationToken);

        // 2. 실제로 사용자 비밀번호 체크
        // MemberService.loadUserByUsername (UserDetailService) 실행됨
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private TokenDto successfulAuthentication(Authentication authentication) {
        // 3. 토큰 생성
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = jwtUtil.createToken(
                "ACCESS"
                , username
                , authorities
                , 600000L);//10분

        String refreshToken = jwtUtil.createToken(
                "REFRESH"
                , username
                , authorities
                , 86400000L); //24시간

        // 4. 저장
        refreshTokenRepository.save(new RefreshToken(username, refreshToken));
        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(jwtUtil.getExpireTime(600000L).getTime())
                .refreshToken(refreshToken)
                .build();
    }
}
