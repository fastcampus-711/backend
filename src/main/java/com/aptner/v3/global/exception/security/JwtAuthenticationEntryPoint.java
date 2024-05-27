package com.aptner.v3.global.exception.security;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Called when an unauthorized access attempt to a protected resource is detected.
     * Sets the HTTP status code to 401 (unauthorized) and sends a message to the client indicating
     * that the access is denied along with the exception message.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // status
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response
        ApiResponse<?> error = ResponseUtil.error(HttpStatus.UNAUTHORIZED, List.of(new String[]{"Authentication Failed: Unable to verify your identity."}));
        String json = objectMapper.writeValueAsString(error);
        // write
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}