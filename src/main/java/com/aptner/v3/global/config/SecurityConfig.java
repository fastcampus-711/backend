package com.aptner.v3.global.config;


import com.aptner.v3.global.jwt.JwtFilter;
import com.aptner.v3.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // jwt 방식으로 session을 STATELESS 상태로 관리하기 때문에 disable 처리
                .csrf(AbstractHttpConfigurer::disable)

                // formLogin, httpBasic은 사용하지 않기 때문에 disable 처리
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        //인가 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                        "/api-docs.html",
                        "/api-docs/**",
                        "/swagger-ui/**",
                        "/actuator/**",
                        "/boards/**").permitAll()
                .requestMatchers(
                        "/auth/**"
                ).permitAll()
                //.requestMatchers("/admin/**").hasRole("ADMIN") //TODO: admin 기능 구현때 주석 해제
                .anyRequest().authenticated());

        http.addFilterBefore(AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtFilter AuthenticationFilter() {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        //h2-console 사용을 위한 설정
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

}
