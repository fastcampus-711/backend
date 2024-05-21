package com.aptner.v3.auth.service;

import com.aptner.v3.auth.RefreshToken;
import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.exception.AttachException;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("비즈니스 로직 - 인증")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService sut;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 토큰 생성")
    public void testLogin_Successful() {

        String username = "username";
        String password = "p@ssword";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        LoginDto loginDto = new LoginDto(username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
        when(jwtUtil.createAccessToken(anyString(), anyString())).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(anyString(), anyString())).thenReturn("refreshToken");

        TokenDto tokenDto = sut.login(loginDto);

        assertNotNull(tokenDto);
        assertEquals("accessToken", tokenDto.accessToken());
        assertEquals("refreshToken", tokenDto.refreshToken());
    }

    @Test
    @DisplayName("새로운 토큰 생성 실패 - userNameNotFoundException")
    public void testLogin_Failure() {

        String username = "invalid";
        String password = "invalid";
        LoginDto loginDto = new LoginDto(username, password);

//        when(loginDto.toAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException(username));

        Throwable exception = assertThrows(UsernameNotFoundException.class, () -> {
            sut.login(loginDto);
        });
    }

    @Test
    @DisplayName("새로운 토큰 생성 실패 - BadCredentialsException")
    public void testLogin_whenWrongPassword_Failure() {

        String username = "username";
        String password = "invalid";
        LoginDto loginDto = new LoginDto(username, password);

//        when(loginDto.toAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException(username));

        Throwable exception = assertThrows(BadCredentialsException.class, () -> {
            sut.login(loginDto);
        });
    }

//    @Test
    @DisplayName("새로운 토큰 생성 실패 - AuthException")
    public void testLogin_whenNull_Failure() {

        // given
        String username = "username";
        String password = "p@ssword";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        LoginDto loginDto = new LoginDto(username, password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
        // when
        when(jwtUtil.createAccessToken(any(), any())).thenReturn(null);
        when(jwtUtil.createRefreshToken(any(), any())).thenReturn(null);

//        Throwable exception = assertThrows(AuthException.class, () -> {
//            sut.login(loginDto);
//        });
    }

    @Test
    public void testReissue_Successful() {
        String accessToken = "validAccessToken";
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        RefreshToken refreshToken = new RefreshToken("username", "validRefreshToken", System.currentTimeMillis() + 60000);

        when(jwtUtil.validateToken(accessToken)).thenReturn(true);
        when(jwtUtil.getAuthentication(accessToken)).thenReturn(authentication);
        when(refreshTokenRepository.findByKey("username")).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.validateToken(refreshToken.getValue())).thenReturn(true);
        when(jwtUtil.createAccessToken(anyString(), anyString())).thenReturn("newAccessToken");
        when(jwtUtil.createRefreshToken(anyString(), anyString())).thenReturn("newRefreshToken");

        TokenDto tokenDto = sut.reissue(accessToken);

        assertNotNull(tokenDto);
        assertEquals("newAccessToken", tokenDto.accessToken());
        assertEquals("newRefreshToken", tokenDto.refreshToken());
    }

    @Test
    public void testReissue_InvalidAccessToken() {
        String invalidAccessToken = "invalidAccessToken";

        when(jwtUtil.validateToken(invalidAccessToken)).thenReturn(false);

        assertThrows(AuthException.class, () -> sut.reissue(invalidAccessToken));
    }

    @Test
    public void testReissue_InvalidRefreshToken() {
        String accessToken = "validAccessToken";
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        RefreshToken refreshToken = new RefreshToken("username", "invalidRefreshToken", System.currentTimeMillis() + 60000);

        when(jwtUtil.validateToken(accessToken)).thenReturn(true);
        when(jwtUtil.getAuthentication(accessToken)).thenReturn(authentication);
        when(refreshTokenRepository.findByKey("username")).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.validateToken(refreshToken.getValue())).thenReturn(false);

        assertThrows(AttachException.class, () -> sut.reissue(accessToken));
    }

//    @Test
//    public void testLogout() {
//        String username = "username";
//        doNothing().when(refreshTokenRepository).deleteByKey(username);
//
//        sut.logout(username);
//
//        verify(refreshTokenRepository, times(1)).deleteByKey(username);
//    }
}