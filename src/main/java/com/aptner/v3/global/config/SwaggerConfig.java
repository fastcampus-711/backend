package com.aptner.v3.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SwaggerConfig {

    public static final Map<String, String[]> GROUPS = Stream.of(
            new SimpleEntry<>("메뉴", new String[]{"/menu/**"}),
            new SimpleEntry<>("회원", new String[]{"/user/**", "/auth/**"}),
            new SimpleEntry<>("게시판", new String[]{
                    "/categories/**",
                    "/notices/**",
                    "/complaints/**",
                    "/frees/**",
                    "/markets/**",
                    "/qnas/**",
                    "/search/**",
                    "/comments/**"}
            )
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    @Bean
    public OpenAPI openAPI() {

        String key = "Access Token";
        String refreshKey = "Refresh Token";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("key").
                addList("refreshKey");

        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(refreshKey);

        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        Components components = new Components()
                .addSecuritySchemes(key, accessTokenSecurityScheme)
                .addSecuritySchemes(refreshKey, refreshTokenSecurityScheme);

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public GroupedOpenApi getMenu() {
        return GroupedOpenApi
                .builder()
                .group("메뉴")
                .pathsToMatch(GROUPS.get("메뉴"))
                .build();
    }

    @Bean
    public GroupedOpenApi getBoard() {
        return GroupedOpenApi
                .builder()
                .group("게시판")
                .pathsToMatch(GROUPS.get("게시판"))
                .build();
    }

    @Bean
    public GroupedOpenApi getUser() {
        return GroupedOpenApi
                .builder()
                .group("회원")
                .pathsToMatch(GROUPS.get("회원"))
                .build();
    }

    @Bean
    public GroupedOpenApi getETC() {

        String[] excludes = GROUPS.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("기타"))
                .flatMap(entry -> Stream.of(entry.getValue()))
                .distinct()
                .toArray(String[]::new);

        return GroupedOpenApi
                .builder()
                .group("기타")
                .pathsToMatch("/**")
                .pathsToExclude(excludes)
                .build();
    }

}
