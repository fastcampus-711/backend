package com.aptner.v3.auth.controller;

import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody LoginDto user) {
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/refresh")
    @Operation(summary = " 토큰 갱신")
    public ResponseEntity<?> reissue(@RequestHeader(AUTHORIZATION_HEADER) TokenDto request) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/revoke")
    @Operation(summary = " 토큰 제거")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.badRequest().build();
        }
        // remove
        authService.logout(authentication.getName());
        response.addHeader(AUTHORIZATION_HEADER, "");

        return ResponseEntity.ok("logout");
    }
}
