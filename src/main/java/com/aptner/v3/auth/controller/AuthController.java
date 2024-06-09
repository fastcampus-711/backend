package com.aptner.v3.auth.controller;

import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.service.AuthService;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

import static com.aptner.v3.global.util.JwtUtil.AUTHORIZATION_HEADER;

@RestController
@Tag(name = "인증")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Async
    @PostMapping("/token")
    @Operation(summary = " 토큰 발급")
    public ApiResponse<?> login(@RequestBody LoginDto user, HttpServletResponse response) {

        TokenDto login = authService.login(user);

        // cookie
        String encodedValue = Base64.getUrlEncoder().encodeToString(login.accessToken().getBytes());
        Cookie cookie = new Cookie("accesstoken", encodedValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효기간 1주일
        response.addCookie(cookie);

        return ResponseUtil.ok(login);
    }

    @PostMapping("/refresh")
    @Operation(summary = " 토큰 갱신")
    public ApiResponse<?> reissue(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken = resolve(authorizationHeader);
        return ResponseUtil.create(authService.reissue(accessToken));
    }

    @PostMapping("/revoke")
    @Operation(summary = " 토큰 제거")
    public ApiResponse<?> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseUtil.error("토큰", ErrorCode.INVALID_REQUEST);
        }
        // remove
        authService.logout(authentication.getName());
        response.addHeader(AUTHORIZATION_HEADER, "");

        return ResponseUtil.ok("logout");
    }
    private String resolve(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
            throw new AuthException(ErrorCode.INVALID_REQUEST);
        }
        return authorizationHeader.substring(7);
    }

}
