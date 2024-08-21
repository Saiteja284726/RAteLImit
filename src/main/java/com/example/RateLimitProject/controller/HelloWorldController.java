package com.example.RateLimitProject.controller;

import  com.example.RateLimitProject.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RateLimit(maxRequests = 5, duration = 60) // 5 requests per 60 seconds
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}
