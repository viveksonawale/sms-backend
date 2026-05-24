package com.sms.society_management.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-IP rate limiter for the login endpoint.
 * Allows 5 login attempts per minute per IP.
 * After 5 failures, the next attempt gets a 429 Too Many Requests.
 */
@Slf4j
@Component
public class LoginRateLimiter {

    // Max 5 login attempts per 1 minute window, per IP
    private static final int    MAX_ATTEMPTS  = 5;
    private static final Duration WINDOW       = Duration.ofMinutes(1);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, this::newBucket);
        boolean allowed = bucket.tryConsume(1);
        if (!allowed) {
            log.warn("Rate limit exceeded for IP: {} — blocking login attempt", ipAddress);
        }
        return allowed;
    }

    private Bucket newBucket(String ip) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(MAX_ATTEMPTS)
                .refillGreedy(MAX_ATTEMPTS, WINDOW)
                .build();
        return Bucket.builder().addLimit(limit).build();
    }
}
