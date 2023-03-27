package com.narola.apiratelimiterdemo;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {


    @GetMapping("/hello")
    @RateLimiter(name = "myRateLimiter")
    public ResponseEntity<String> getResponse() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Hello world!!!");
    }
}
