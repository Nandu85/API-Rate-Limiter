package com.narola.apiratelimiterdemo.ratelimiter;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
