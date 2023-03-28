package com.narola.apiratelimiterdemo.interceptor;

import com.narola.apiratelimiterdemo.ratelimiter.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {
    private static final Map<String, Bucket> buckets = new HashMap<>();

    private static final int DEFAULT_CAPACITY = 5;
    private static final Duration DEFAULT_REFILL_PERIOD = Duration.ofMinutes(1); // 1 Minutes
    private static final int DEFAULT_REFILL_TOKENS = 5; // 5 tokens per second

    public static Bucket createBucket(int capacity, Duration refillPeriod, int refillTokens) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(refillTokens, refillPeriod));
        return new LocalBucketBuilder()
                .addLimit(limit)
                .build();
    }

    public static synchronized Bucket getBucketForUser(String username) {
        return buckets.computeIfAbsent(username, k -> createBucket(DEFAULT_CAPACITY, DEFAULT_REFILL_PERIOD, DEFAULT_REFILL_TOKENS));
    }

    public static synchronized void setRateLimitsForUser(String username, int capacity, Duration refillPeriod, int refillTokens) {
        buckets.put(username, createBucket(capacity, refillPeriod, refillTokens));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = request.getHeader("username");
        int limit = DEFAULT_REFILL_TOKENS;
        Bucket bucket = getBucketForUser(key);
        if (bucket == null) {
            setRateLimitsForUser(key, limit, DEFAULT_REFILL_PERIOD, limit);
            bucket = getBucketForUser(key);
        }
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            throw new RateLimitExceededException("Rate limit exceeded for user " + key
                    + ", wait for " + waitForRefill + " seconds");
        }
    }
}
