package com.aptner.v3.global.jwt;

import com.aptner.v3.security.dto.CustomUserDetailsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public LoginFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //formLogin방식을 쓰지않기 때문에 UsernamePasswordAuthenticationFilter 커스텀 필터를 만들어준다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //request에서 username과 password를 받아서 Authentication객체로 만들어주는 메소드
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //TODO: username, password, ROlE을 담아서 넘겨줄건데 이부분은 추후에 구현해야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //최종적으로 검증을 담당할 AuthenticationManager에게 전달
        return getAuthenticationManager().authenticate(authToken);
    }

    //로그인 성공시에 실행되는 메소드
    @Override
    public void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        //로그인 성공시 JWT토큰을 생성해서 응답해주는 메소드
        System.out.println("successfulAuthentication 호출 : 로그인 성공");

        CustomUserDetailsDto customUserDetailsDto = (CustomUserDetailsDto) authResult.getPrincipal();
        String username = customUserDetailsDto.getUsername();
        //String role = customUserDetailsDto.getAuthorities().toString();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String role = grantedAuthority.getAuthority();

        String token = jwtUtil.createToken(username, role, 1000L * 60 * 60 * 24 * 7); //7일

        //Authorization 키에 Bearer 토큰값을 넣어서 응답
        //"Bearer " Bearer 뒤에 공백을 넣어줘야함
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //로그인 실패시에 실행되는 메소드
        System.out.println("unsuccessfulAuthentication 호출 : 로그인 실패");
        //로그인 실패시 401에러를 반환
        response.setStatus(401);
    }
}
