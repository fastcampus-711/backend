package com.aptner.v3.global.jwt;

import com.aptner.v3.user.domain.User;
import com.aptner.v3.user.dto.CustomUserDetailsDto;
import com.aptner.v3.user.type.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter 는 한번의 요청에 대해서 한번만 수행하도록 보장하는 필터
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        //1. 헤더에 토큰이 있는지 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("JwtFilter.doFilterInternal 토큰 없음");

            //다음 필터로 넘겨줌
            filterChain.doFilter(request, response);

            //해당 조건이 만족될 경우 메소드 종료
            return;
        }
        //Bearer 접두사 제거
        String token = authorizationHeader.substring(7);

        // 2.토큰 소멸시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("JwtFilter.doFilterInternal 토큰 만료");
            filterChain.doFilter(request, response);
            return;
        }

        // 검증 수행후 토큰에서 유저정보를 가져옴
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(username);
        // password의 경우 token에 담겨있지 않기때문에 매번 요청마다 db를 조회를 하는 상황을 방지하기위해 password는 임시로 설정
        //user.setPassword("tempPassword12!@");
        user.setRoles(UserRole.valueOf(role));

        CustomUserDetailsDto customUserDetailsDto = new CustomUserDetailsDto(user);
        //검증된 토큰 정보를 기반으로 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetailsDto, null, customUserDetailsDto.getAuthorities());
        //SecurityContextHolder에 Authentication 객체 저장
        //SESSION을 STATELESS 로 사용하기 때문에 SecurityContextHolder에 저장해놓아야함
        //이후 SecurityContextHolder를 통해 Authentication 객체를 가져올 수 있음
        //이를 통해 로그인한 사용자의 정보를 가져올 수 있음
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //security 필터로 넘겨줌
        filterChain.doFilter(request, response);
    }
}
