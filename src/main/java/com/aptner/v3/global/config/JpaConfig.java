package com.aptner.v3.global.config;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
//@EnableJpaRepositories
public class JpaConfig {

//    @Bean
//    JPAQueryFactory jPAQueryFactory(EntityManager em) {
//        // queryDsl
//        return new JPAQueryFactory(em);
//    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditor();
    }

    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

    @Slf4j
    public static class SecurityAuditor implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .filter(principal -> principal instanceof CustomUserDetails)
                    .map(principal -> ((CustomUserDetails) principal).getUsername())
                    .orElse("system"));
        }
    }
}
