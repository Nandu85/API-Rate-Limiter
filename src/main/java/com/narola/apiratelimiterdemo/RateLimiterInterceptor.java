package com.narola.apiratelimiterdemo;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter = RateLimiter.create(0.5);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (rateLimiter.tryAcquire(1))
        {
            return true;
        }
//        long waitForRefill = rateLimiter.getRate()
//        response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
        response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                "You have exhausted your API Request Quota");
        return false;
    }
}
