package com.aptner.v3.global.config;

import com.aptner.v3.global.jwt.JwtFilter;
import com.aptner.v3.global.jwt.JwtUtil;
import com.aptner.v3.global.jwt.LoginFilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean //h2-console 사용을 위한 설정
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { // 비밀번호 암호화를 위한 Bean 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // jwt 방식으로 session을 STATELESS 상태로 관리하기 때문에 disable 처리
        http.csrf(auth -> auth.disable());

        // formLogin, httpBasic은 사용하지 않기 때문에 disable 처리
        http.formLogin(auth -> auth.disable());
        http.httpBasic(auth -> auth.disable());

        //인가 설정
        http.authorizeHttpRequests(auth -> auth
                 .requestMatchers("/login/**","/","/signup/**").permitAll() // login, /, signUp은 누구나 접근 가능
                //.requestMatchers("/admin/**").hasRole("ADMIN") //TODO: admin 기능 구현때 주석 해제
                .anyRequest().authenticated()); // 나머지 요청은 인증된 사용자만 접근 가능
                //.anyRequest().permitAll()); //TODO: security 설정 테스트를 위해 임시로 permitAll로 설정

        // JWT 인증 필터를 LoginFilter 전에 넣어줌
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 넣어줌
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
