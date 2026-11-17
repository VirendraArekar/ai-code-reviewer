package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.auth.*;
import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.RefreshToken;
import com.aicode.reviewer.entity.User;
import com.aicode.reviewer.entity.enums.Role;
import com.aicode.reviewer.repository.OrganizationMemberRepository;
import com.aicode.reviewer.repository.OrganizationRepository;
import com.aicode.reviewer.repository.RefreshTokenRepository;
import com.aicode.reviewer.repository.UserRepository;
import com.aicode.reviewer.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMemberRepository organizationMemberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .email("developer@example.com")
                .passwordHash("hashed_password")
                .fullName("John Doe")
                .role(Role.DEVELOPER)
                .emailVerified(true)
                .build();
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("Jane Doe", "jane@example.com", "Password123!", "Acme Corp");

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(organizationRepository.save(any(Organization.class))).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtTokenProvider.generateAccessToken(any(User.class))).thenReturn("jwt-token-123");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        AuthResponse response = authService.register(request, "127.0.0.1");

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getAccessToken());
        assertEquals("Jane Doe", response.getUser().getFullName());
        assertEquals("jane@example.com", response.getUser().getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterDuplicateEmailThrows() {
        RegisterRequest request = new RegisterRequest("Jane Doe", "jane@example.com", "Password123!", "Acme Corp");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request, "127.0.0.1"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("developer@example.com", "Password123!");

        when(userRepository.findByEmail("developer@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Password123!", "hashed_password")).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(testUser)).thenReturn("jwt-token-456");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        AuthResponse response = authService.login(request, "127.0.0.1");

        assertNotNull(response);
        assertEquals("jwt-token-456", response.getAccessToken());
        assertEquals("developer@example.com", response.getUser().getEmail());
    }

    @Test
    void testLoginInvalidPasswordThrows() {
        LoginRequest request = new LoginRequest("developer@example.com", "WrongPassword!");

        when(userRepository.findByEmail("developer@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("WrongPassword!", "hashed_password")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(request, "127.0.0.1"));
    }

    @Test
    void testRefreshTokenRotationSuccess() {
        RefreshToken token = RefreshToken.builder()
                .id("token-id")
                .token("valid-refresh-token")
                .user(testUser)
                .familyId("fam-1")
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        when(refreshTokenRepository.findByToken("valid-refresh-token")).thenReturn(Optional.of(token));
        when(jwtTokenProvider.generateAccessToken(testUser)).thenReturn("new-jwt-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        AuthResponse response = authService.refreshToken(new RefreshTokenRequest("valid-refresh-token"));

        assertNotNull(response);
        assertEquals("new-jwt-token", response.getAccessToken());
        assertTrue(token.isRevoked(), "Old refresh token must be revoked during rotation");
    }

    @Test
    void testChangePasswordSuccess() {
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword123!", "NewPassword123!");

        when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("OldPassword123!", "hashed_password")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("new_hashed_password");

        authService.changePassword("user-123", request);

        assertEquals("new_hashed_password", testUser.getPasswordHash());
        verify(userRepository).save(testUser);
    }
}
