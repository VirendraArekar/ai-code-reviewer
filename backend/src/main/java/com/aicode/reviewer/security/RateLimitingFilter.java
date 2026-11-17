package com.aicode.reviewer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * High-performance sliding window rate limiter filter.
 * Protects critical auth and webhook endpoints from brute-force and DoS attacks.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Rate limit authentication and webhook endpoints
        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/webhooks")) {
            String clientIp = getClientIp(request);
            long currentMinute = System.currentTimeMillis() / 60000;
            String key = clientIp + ":" + currentMinute;

            RequestCounter counter = requestCounts.computeIfAbsent(key, k -> new RequestCounter());
            if (counter.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Too many requests. Rate limit exceeded. Try again in 60 seconds.\"}");
                return;
            }

            // Cleanup old counters periodically (keep map bounded)
            if (requestCounts.size() > 5000) {
                requestCounts.entrySet().removeIf(entry -> {
                    try {
                        long minute = Long.parseLong(entry.getKey().split(":")[1]);
                        return minute < currentMinute - 2;
                    } catch (Exception e) {
                        return true;
                    }
                });
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "127.0.0.1";
    }

    public static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        public int incrementAndGet() {
            return count.incrementAndGet();
        }

        public int get() {
            return count.get();
        }
    }
}
