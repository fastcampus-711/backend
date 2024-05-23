package com.aptner.v3.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    // FreePost의 7일 이내 조회수+공감수가 가장 높은 3개의 글을 조회하는 메서드를 위한 Config
}
