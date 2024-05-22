package com.aptner.v3.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    //TODO : HandlerExceptionResolver @Qualifier("handlerExceptionResolver") 의존성 주입, final이 선언되어있으면 에러 발생
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.debug("{]", "JwtAccessDeniedHandler");
//        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);

        response.setCharacterEncoding("utf-8");
        response.sendError(403, "권한이 없습니다.");
    }

}
