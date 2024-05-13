package com.aptner.v3.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    //private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //logout경로 검증
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("/logout")) {
            chain.doFilter(request, response);
            return;
        }
        //POST요청인지 검증
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")){
            chain.doFilter(request, response);
            return;
        }
        //refresh 쿠키 검증
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                //break;
            }
        }

        //refresh 쿠키가 없으면 400응답
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //refresh 만료 검증
        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        /*//token이 access, refresh 구별
        String tokenType = jwtUtil.getTokenType(refresh);
        if (!tokenType.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //token이 DB에 저장되어 있는지 검증
        String existToken = refreshRepository.existsByToken(refresh);
        if(!existToken){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        refreshRepository.deleteByToken(refresh);*/

        //로그아웃 진행
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        //cookie null로 초기화
        response.addCookie(cookie);
        //200응답
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
