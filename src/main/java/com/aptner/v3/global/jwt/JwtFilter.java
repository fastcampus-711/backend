package com.aptner.v3.global.jwt;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.exception.AuthException;
import com.aptner.v3.global.util.JwtUtil;
import com.aptner.v3.global.util.ResponseUtil;
import com.aptner.v3.member.Member;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.aptner.v3.global.util.JwtUtil.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header 에서 토큰을 꺼냄
        String token = resolveToken(request);
        try {
            if (StringUtils.hasText(token) && !jwtUtil.isExpired(token)) {

                Claims claims = jwtUtil.parseClaims(token);
                Member member = jwtUtil.claimsToMember(claims);
                if (!isLogout(member.getUsername())) {

                    // 2. security Context 저장
                    CustomUserDetails userDetails = new CustomUserDetails(member);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception e) {
            handleException(response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLogout(String username) {
        boolean exists = refreshTokenRepository.existsByKey(username);
        if (!exists) {
            throw new AuthException(ErrorCode.NOT_AVAILABLE_TOKEN);
        }
        return exists;
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
