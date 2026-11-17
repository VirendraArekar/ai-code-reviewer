package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.auth.*;
import com.aicode.reviewer.entity.Organization;
import com.aicode.reviewer.entity.OrganizationMember;
import com.aicode.reviewer.entity.RefreshToken;
import com.aicode.reviewer.entity.User;
import com.aicode.reviewer.entity.enums.Role;
import com.aicode.reviewer.repository.OrganizationMemberRepository;
import com.aicode.reviewer.repository.OrganizationRepository;
import com.aicode.reviewer.repository.RefreshTokenRepository;
import com.aicode.reviewer.repository.UserRepository;
import com.aicode.reviewer.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    public AuthService(UserRepository userRepository, OrganizationRepository organizationRepository, OrganizationMemberRepository organizationMemberRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.organizationMemberRepository = organizationMemberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditLogService = auditLogService;
    }


    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;

    @Transactional
    public AuthResponse login(LoginRequest request, String ipAddress) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshTokenStr = UUID.randomUUID().toString();
        String familyId = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .token(refreshTokenStr)
                .user(user)
                .familyId(familyId)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        auditLogService.log(user.getId(), user.getEmail(), "USER_LOGIN", "USER", user.getId(), ipAddress, "Successful login");

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .expiresIn(86400000L)
                .user(mapToDto(user))
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, String ipAddress) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        String orgName = request.getOrganizationName() != null && !request.getOrganizationName().isBlank()
                ? request.getOrganizationName()
                : request.getFullName() + "'s Org";

        String slug = orgName.toLowerCase().replaceAll("[^a-z0-9]", "-") + "-" + UUID.randomUUID().toString().substring(0, 6);

        Organization org = Organization.builder()
                .id(UUID.randomUUID().toString())
                .name(orgName)
                .slug(slug)
                .plan("FREE")
                .build();
        organizationRepository.save(org);

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.ORGANIZATION_OWNER)
                .emailVerified(true)
                .organization(org)
                .build();
        userRepository.save(user);

        org.setOwnerId(user.getId());
        organizationRepository.save(org);

        OrganizationMember member = OrganizationMember.builder()
                .id(UUID.randomUUID().toString())
                .organization(org)
                .user(user)
                .role(Role.ORGANIZATION_OWNER)
                .build();
        organizationMemberRepository.save(member);

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshTokenStr = UUID.randomUUID().toString();
        String familyId = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .token(refreshTokenStr)
                .user(user)
                .familyId(familyId)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        auditLogService.log(user.getId(), user.getEmail(), "USER_REGISTERED", "USER", user.getId(), ipAddress, "Account created with organization: " + orgName);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .expiresIn(86400000L)
                .user(mapToDto(user))
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            // Compromised token reuse detected: revoke entire family!
            refreshTokenRepository.revokeFamily(token.getFamilyId());
            throw new BadCredentialsException("Compromised or expired token. Family revoked.");
        }

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        User user = token.getUser();
        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRefreshTokenStr = UUID.randomUUID().toString();

        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .token(newRefreshTokenStr)
                .user(user)
                .familyId(token.getFamilyId())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .expiresIn(86400000L)
                .user(mapToDto(user))
                .build();
    }

    @Transactional
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Incorrect current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        return mapToDto(user);
    }

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .emailVerified(user.isEmailVerified())
                .githubUsername(user.getGithubUsername())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
