package com.aicode.reviewer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RateLimitingFilterTest {

    private RateLimitingFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RateLimitingFilter();
    }

    @Test
    void testNormalTrafficAllowed() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        request.setRemoteAddr("192.168.1.100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        assertEquals(200, response.getStatus());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testExcessiveRequestsThrottled() throws ServletException, IOException {
        String clientIp = "10.0.0.99";
        FilterChain filterChain = mock(FilterChain.class);

        // Send 60 allowed requests
        for (int i = 0; i < 60; i++) {
            MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/v1/auth/login");
            req.setRemoteAddr(clientIp);
            MockHttpServletResponse res = new MockHttpServletResponse();
            filter.doFilter(req, res, filterChain);
            assertEquals(200, res.getStatus());
        }

        // 61st request should be rejected with 429
        MockHttpServletRequest req61 = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        req61.setRemoteAddr(clientIp);
        MockHttpServletResponse res61 = new MockHttpServletResponse();
        filter.doFilter(req61, res61, filterChain);

        assertEquals(429, res61.getStatus(), "Should return 429 Too Many Requests");
        // Verify filterChain was NOT called for 61st request
        verify(filterChain, times(60)).doFilter(any(), any());
    }

    @Test
    void testNonProtectedEndpointsBypassRateLimiter() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/public/health");
        request.setRemoteAddr("10.0.0.99");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        assertEquals(200, response.getStatus());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
