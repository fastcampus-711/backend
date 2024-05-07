package com.aptner.v3.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //formLogin방식을 쓰지않기 때문에 UsernamePasswordAuthenticationFilter를 커스텀 필터를 만들어준다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //request에서 username과 password를 받아서 Authentication객체로 만들어주는 메소드
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //TODO: username, password, ROlE을 담아서 넘겨줄건데 이부분은 추후에 구현해야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //최종적으로 검증을 담당할 AuthenticationManager에게 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시에 실행되는 메소드
    @Override
    public void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //로그인 성공시 JWT토큰을 생성해서 응답해주는 메소드

    }
}
