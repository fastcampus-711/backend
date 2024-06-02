package com.aptner.v3.global.jwt;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.global.util.ResponseUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.aptner.v3.CommunityApplication.passwordEncoder;
import static com.aptner.v3.global.util.JwtUtil.BEARER_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${server.auth}")
    private Boolean isAuth;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header 에서 토큰을 꺼냄
        String token = resolveToken(request);
        log.debug("isAuth: {}", isAuth);
        try {
            if (!isAuth || (StringUtils.hasText(token) && !jwtUtil.isExpired(token))) {

                Member member = null;
                if (isAuth) {
                    Claims claims = jwtUtil.parseClaims(token);
                    member = jwtUtil.claimsToMember(claims);
                } else {
                    // @test
                    member = Member.of("user", passwordEncoder().encode("p@ssword"), "nickname1", "https://avatars.githubusercontent.com/u/79270228?v=4", "01011112222", List.of(MemberRole.ROLE_USER));
                    member.setId(1L);
                }
                log.debug("토큰으로 부터 가져온 정보 : {}", member);
                if (!isAuth || (isRefreshTokenExists(member.getUsername()))) {

                    // 2. security Context 저장
                    log.debug("유효한 토큰, 정상 접근 {}", token);
                    CustomUserDetails principal = new CustomUserDetails(member);
                    UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception e) {
            if (isAuth) {
                handleException(response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isRefreshTokenExists(String username) {
        return refreshTokenRepository.existsByKey(username);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        SecurityContextHolder.clearContext(); // Clear context for any security exceptions

        String errorMessage;
        int httpStatus = HttpServletResponse.SC_FORBIDDEN; // Default HTTP status code

        if (e instanceof IncorrectClaimException) {
            log.debug("Invalid JWT token.");
            errorMessage = "Invalid JWT token: Your request cannot be processed.";
        } else if (e instanceof UsernameNotFoundException || e instanceof IllegalArgumentException) {
            log.debug("User related error: {} ", e.getMessage());
            errorMessage = "Access Denied: No user found with the provided credentials.";
        } else if (e instanceof ExpiredJwtException) {
            log.debug("Expired JWT token.");
            httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
            errorMessage = "Token has expired: Please log in again.";
        } else if (e instanceof SignatureException) {
            log.debug("Authentication Failed. Username or Password not valid.");
            errorMessage = "Authentication failed: Username or Password not valid.";
        } else {
            log.debug("Unknown authentication error.");
            log.debug(e.getMessage());
            errorMessage = "Authentication error: Unable to process your request.";
        }

        sendErrorResponse(response, httpStatus, errorMessage);
    }

    private void sendErrorResponse(HttpServletResponse response, int httpStatus, String errorMessage) throws IOException {
        // clear
        SecurityContextHolder.clearContext();
        // status
        response.setContentType("application/json");
        response.setStatus(httpStatus);
        // response
        ApiResponse<?> errorResponse = ResponseUtil.error(HttpStatus.valueOf(httpStatus), List.of(errorMessage));
        String json = new ObjectMapper().writeValueAsString(errorResponse);
        // write
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
