//package com.narola.apiratelimiterdemo;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Refill;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Duration;
//
//@Configuration
//public class BucketConfig {
//    //It fills 1 token at every 12 seconds in bucket with capacity of 10. Because, 120/10=12
//    @Bean
//    public Bucket newBucket() {
//        return Bucket.builder()
//                .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(2))))
//                .build();
//    }
//}
