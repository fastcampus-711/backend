package com.aptner.v3.global.config;


import com.aptner.v3.global.jwt.JwtFilter;
import com.aptner.v3.global.jwt.JwtUtil;
import com.aptner.v3.global.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final AuthenticationConfiguration authenticationConfiguration;

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
                        "/actuator/**").permitAll()
                .requestMatchers(
                        "/signup",
                        "/login",
                        "/**"
                ).permitAll()
                //.requestMatchers("/admin/**").hasRole("ADMIN") //TODO: admin 기능 구현때 주석 해제
                .anyRequest().authenticated());

        // JWT 인증 필터를 LoginFilter 전에 넣어줌
        http.addFilterBefore(AuthenticationFilter(), LoginFilter.class);
        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 넣어줌
        http.addFilterAt(LoginFilter(), UsernamePasswordAuthenticationFilter.class);

        //http.addFilterBefore(new CustomLogoutFilter(jwtUtil,refreshRepository), LogoutFilter.class);
        return http.build();
    }

    @Bean
    public JwtFilter AuthenticationFilter() {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public LoginFilter LoginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(jwtUtil);
        loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return loginFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        //h2-console 사용을 위한 설정
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

}
