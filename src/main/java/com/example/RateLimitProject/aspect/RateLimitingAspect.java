package com.example.RateLimitProject.aspect;

import  com.example.RateLimitProject.annotation.RateLimit;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitingAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(rateLimit)")
    public void rateLimiter(RateLimit rateLimit) {
        String clientIP = request.getRemoteAddr();
        String key = "rate_limit_" + clientIP;

        Long currentRequests = redisTemplate.opsForValue().increment(key, 1);
        if (currentRequests == 1) {
            redisTemplate.expire(key, rateLimit.duration(), TimeUnit.SECONDS);
        }

        if (currentRequests != null && currentRequests > rateLimit.maxRequests()) {
            throw new RuntimeException("Rate limit exceeded. Please try again later.");
        }
    }
}
