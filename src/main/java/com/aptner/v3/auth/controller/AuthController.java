package com.aptner.v3.auth.controller;

import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.service.AuthService;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.aptner.v3.global.util.JwtUtil.AUTHORIZATION_HEADER;

@RestController
@Tag(name = "인증")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    @Operation(summary = " 토큰 발급")
    public ApiResponse<?> login(@RequestBody LoginDto user) {
        return ResponseUtil.ok(authService.login(user));
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/test")
    public ApiResponse<?> adminOnly() {
        return ResponseUtil.ok("어드민 접속 완료");
    }

    /* 이거 허용 되면 안됨 */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/admin/user")
    public ApiResponse<?> userOnly() {
        return ResponseUtil.ok("회원 접속 완료");
    }
}
