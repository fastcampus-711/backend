package com.aptner.v3.global.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class CorsConfig {
    public static Customizer<CorsConfigurer<HttpSecurity>> corsFilter(String frontEndUrl) {
        return cors -> cors
                .configurationSource(corsConfigurationSource(frontEndUrl));
    }

    private static CorsConfigurationSource corsConfigurationSource(String frontEndUrl) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontEndUrl);
        configuration.addAllowedOrigin("localhost:3000");
        configuration.addAllowedOrigin("localhost");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}