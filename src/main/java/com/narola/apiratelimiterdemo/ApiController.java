package com.narola.apiratelimiterdemo;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Date;

@RestController
public class ApiController {

    private final Bucket bucket;

    public ApiController() {
        Bandwidth bandwidth = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(2)));
        this.bucket = Bucket.builder().addLimit(bandwidth).build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getResponse() {
        System.out.println(new Date());
        if (bucket.tryConsume(1))
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Scope remaining", String.valueOf(bucket.getAvailableTokens()))
                    .body("Hello world!!!");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Scope remaining", String.valueOf(bucket.getAvailableTokens()))
                .build();
    }
}
