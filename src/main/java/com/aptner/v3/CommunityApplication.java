package com.aptner.v3;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

//    @Bean
//    public static ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper;
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// 비밀번호 암호화를 위한 Bean 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    JPAQueryFactory jPAQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
