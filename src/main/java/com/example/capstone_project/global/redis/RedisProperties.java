package com.example.capstone_project.global.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*
    ConfigurationProperties를 이용해서 spring.redis의
    하위 값들을 필드로 가져온다.
 */
@Component
@Getter
@PropertySource("application.yaml")
public class RedisProperties {
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;
}
