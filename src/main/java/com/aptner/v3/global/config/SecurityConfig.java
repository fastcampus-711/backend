package com.aptner.v3.global.config;


import com.aptner.v3.auth.repository.RefreshTokenRepository;
import com.aptner.v3.global.exception.security.JwtAccessDeniedHandler;
import com.aptner.v3.global.exception.security.JwtAuthenticationEntryPoint;
import com.aptner.v3.global.jwt.JwtFilter;
import com.aptner.v3.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aptner.v3.global.config.CorsConfig.corsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // jwt 방식으로 session을 STATELESS 상태로 관리하기 때문에 disable 처리
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsFilter(frontendUrl))
                // formLogin, httpBasic은 사용하지 않기 때문에 disable 처리
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(ex -> ex.accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );

        //인가 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                        "/api-docs.html",
                        "/api-docs/**",
                        "/swagger-ui/**",
                        "/actuator/**"
                        ).permitAll()
                .requestMatchers(
                        "/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/**", "/user/signup").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.addFilterBefore(AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtFilter AuthenticationFilter() {
        return new JwtFilter(jwtUtil, refreshTokenRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        //h2-console 사용을 위한 설정
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

}
