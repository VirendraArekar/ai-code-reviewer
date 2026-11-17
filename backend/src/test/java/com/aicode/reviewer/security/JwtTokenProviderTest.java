package com.aicode.reviewer.security;

import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.User;
import com.aicode.reviewer.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String testSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        jwtTokenProvider = new JwtTokenProvider(testSecret, 3600000L);
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        Organization org = Organization.builder().id("org-1").name("Acme").build();
        User user = User.builder()
                .id("u-123")
                .email("test@aicode.com")
                .role(Role.DEVELOPER)
                .organization(org)
                .build();

        String token = jwtTokenProvider.generateAccessToken(user);
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));

        assertEquals("test@aicode.com", jwtTokenProvider.getEmailFromToken(token));
        assertEquals("u-123", jwtTokenProvider.getUserIdFromToken(token));
        assertEquals("DEVELOPER", jwtTokenProvider.getRoleFromToken(token));
    }

    @Test
    void shouldRejectInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.jwt.token"));
    }
}
