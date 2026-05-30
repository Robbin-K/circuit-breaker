package com.robbin.user_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class HealthPublisher {

    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 5000)
    public void publishHealth() {

        redisTemplate.opsForValue().set(
                "health:user-service",
                "UP",
                Duration.ofSeconds(10)
        );

        System.out.println(
                "Published user-service UP"
        );
    }
}
