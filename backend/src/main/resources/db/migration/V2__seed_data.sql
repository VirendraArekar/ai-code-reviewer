-- ========================================================
-- Seed Data for Development & Testing
-- Password for all seed users is 'AdminPassword123!'
-- BCrypt Hash: $2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy
-- ========================================================

-- Insert Default Organization
INSERT INTO organizations (id, name, slug, plan, owner_id)
VALUES ('org-1', 'Acme Engineering', 'acme-engineering', 'ENTERPRISE', 'u-1')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert Default Super Admin and Developers
INSERT INTO users (id, email, password_hash, full_name, role, email_verified, organization_id)
VALUES 
('u-1', 'admin@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Virendra Arekar', 'SUPER_ADMIN', TRUE, 'org-1'),
('u-2', 'alex@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Alex Rivera', 'TEAM_LEAD', TRUE, 'org-1'),
('u-3', 'sarah@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Sarah Chen', 'DEVELOPER', TRUE, 'org-1')
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Insert Organization Members
INSERT INTO organization_members (id, organization_id, user_id, role)
VALUES
('om-1', 'org-1', 'u-1', 'SUPER_ADMIN'),
('om-2', 'org-1', 'u-2', 'TEAM_LEAD'),
('om-3', 'org-1', 'u-3', 'DEVELOPER')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- Insert Teams
INSERT INTO teams (id, organization_id, name, description)
VALUES
('team-1', 'org-1', 'Backend Platform', 'Core microservices, auth, databases, and API gateways'),
('team-2', 'org-1', 'Frontend & Mobile', 'Web dashboards, mobile apps, and design systems')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert Team Members
INSERT INTO team_members (id, team_id, user_id, role)
VALUES
('tm-1', 'team-1', 'u-2', 'TEAM_LEAD'),
('tm-2', 'team-1', 'u-3', 'DEVELOPER')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- Insert Repositories
INSERT INTO repositories (id, organization_id, github_repo_id, name, full_name, owner_login, default_branch, private_repo, html_url, primary_language, auto_review_enabled, inline_comments_enabled, review_mode, min_severity, ai_provider, ai_model, max_diff_size_kb, excluded_patterns, custom_instructions)
VALUES
('repo-1', 'org-1', 101, 'api-gateway', 'acme/api-gateway', 'acme', 'main', TRUE, 'https://github.com/acme/api-gateway', 'Java', TRUE, TRUE, 'DEEP', 'MEDIUM', 'GEMINI', 'gemini-2.0-flash', 500, '*.min.js, *.lock, package-lock.json', 'Enforce Spring Boot best practices. Verify all endpoints authenticate with JWT. Flag any non-parameterized SQL queries.'),
('repo-2', 'org-1', 102, 'auth-service', 'acme/auth-service', 'acme', 'main', TRUE, 'https://github.com/acme/auth-service', 'TypeScript', TRUE, TRUE, 'SECURITY_FOCUSED', 'LOW', 'OPENAI', 'gpt-4o', 800, '*.min.js, dist/**', 'Prioritize OWASP vulnerabilities, strict type assertions, and token validation.'),
('repo-3', 'org-1', 103, 'payment-service', 'acme/payment-service', 'acme', 'main', TRUE, 'https://github.com/acme/payment-service', 'Go', TRUE, FALSE, 'STANDARD', 'HIGH', 'GEMINI', 'gemini-2.0-flash', 600, 'vendor/**', 'Enforce idempotent payment transitions and strict HMAC signature validation.')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert AI Provider Configs
INSERT INTO ai_provider_configs (id, organization_id, provider, model_name, is_default, enabled, base_url)
VALUES
('p-1', 'org-1', 'GEMINI', 'gemini-2.0-flash', TRUE, TRUE, 'https://generativelanguage.googleapis.com'),
('p-2', 'org-1', 'OPENAI', 'gpt-4o', FALSE, TRUE, 'https://api.openai.com/v1'),
('p-3', 'org-1', 'OLLAMA', 'deepseek-coder:6.7b', FALSE, TRUE, 'http://localhost:11434')
ON DUPLICATE KEY UPDATE model_name=VALUES(model_name);

-- Insert Pull Requests
INSERT INTO pull_requests (id, repository_id, github_pr_number, title, description, author_username, state, source_branch, target_branch, html_url, additions, deletions, changed_files_count, quality_score, security_score)
VALUES
('pr-1', 'repo-1', 42, 'feat: add OAuth2 refresh token rotation & revocation endpoints', 'Implements RFC 6749 compliant token rotation.', 'alexdev', 'OPEN', 'feat/oauth2-rotation', 'main', 'https://github.com/acme/api-gateway/pull/42', 342, 48, 4, 89, 84),
('pr-2', 'repo-1', 89, 'refactor: switch user permission check to batch SQL fetch to avoid N+1 queries', 'Replaces eager iteration with single indexed query.', 'johndoe', 'OPEN', 'perf/batch-permissions', 'main', 'https://github.com/acme/api-gateway/pull/89', 120, 95, 2, 96, 98),
('pr-3', 'repo-2', 15, 'fix: validate external webhook signature with HMAC-SHA256 constant time comparison', 'Fixes timing attack vulnerability in signature verification.', 'sarah_sec', 'OPEN', 'sec/fix-timing-attack', 'main', 'https://github.com/acme/auth-service/pull/15', 54, 12, 1, 74, 70)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- Insert Pull Request Files
INSERT INTO pull_request_files (id, pull_request_id, filename, status, additions, deletions, patch, findings_count)
VALUES
('file-1', 'pr-1', 'src/main/java/com/aicode/security/RefreshTokenService.java', 'MODIFIED', 120, 15, '@@ -45,8 +45,18 @@ public class RefreshTokenService {\n-    public Token verify(String rawToken) {\n-        return tokenRepo.findByToken(rawToken);\n+    @Transactional\n+    public Token verifyAndRotate(String rawToken) {\n+        Token token = tokenRepo.findByToken(rawToken)\n+            .orElseThrow(() -> new InvalidTokenException(\"Token not found\"));\n+        if (token.isRevoked()) {\n+            tokenRepo.revokeFamily(token.getFamilyId());\n+            throw new CompromisedTokenException(\"Token reuse detected!\");\n+        }\n+        token.setRevoked(true);\n+        return createNewToken(token.getUser(), token.getFamilyId());\n+    }', 2),
('file-2', 'pr-1', 'src/main/java/com/aicode/controller/AuthController.java', 'MODIFIED', 45, 8, '@@ -22,6 +22,12 @@ public class AuthController {\n+    @PostMapping(\"/refresh\")\n+    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRequest req) {\n+        return ResponseEntity.ok(tokenService.verifyAndRotate(req.refreshToken()));\n+    }', 1)
ON DUPLICATE KEY UPDATE filename=VALUES(filename);

-- Insert AI Reviews
INSERT INTO ai_reviews (id, pull_request_id, summary, quality_score, security_score, performance_score, architecture_score, test_score, ai_provider, ai_model, duration_ms, token_count)
VALUES
('rev-1', 'pr-1', 'Autonomous analysis completed across 4 changed files and 390 line additions/deletions. Identified 1 potential high-severity race condition and 2 performance improvements.', 89, 84, 92, 88, 75, 'GEMINI', 'gemini-2.0-flash', 14200, 4120)
ON DUPLICATE KEY UPDATE summary=VALUES(summary);

-- Insert Review Findings
INSERT INTO review_findings (id, ai_review_id, pull_request_id, repository_id, file_path, line_number, title, description, severity, category, technical_explanation, why_it_matters, potential_impact, recommended_solution, original_code_snippet, corrected_code_snippet, confidence_score, status)
VALUES
('find-1', 'rev-1', 'pr-1', 'repo-1', 'src/main/java/com/aicode/security/RefreshTokenService.java', 48, 'Potential Race Condition in Token Rotation Validation', 'Concurrent requests with the same refresh token may bypass the revocation check due to missing lock.', 'HIGH', 'SECURITY', 'Parallel HTTP requests can read revoked = false before the transaction updates and commits.', 'Allows session hijacking.', 'Authentication compromise.', 'Apply pessimistic write lock or unique constraint.', 'Token token = tokenRepo.findByToken(rawToken);', 'Token token = tokenRepo.findByTokenWithLock(rawToken);', 0.940, 'OPEN'),
('find-2', 'rev-1', 'pr-1', 'repo-1', 'src/main/java/com/aicode/controller/AuthController.java', 24, 'Missing Rate Limiter on Authentication Refresh Endpoint', 'The /refresh endpoint does not enforce IP or client-level rate limiting.', 'MEDIUM', 'PERFORMANCE', 'Unauthenticated or automated requests can flood token revocation queries, degrading DB performance.', 'Protects service from DoS and brute force scanning.', 'Database thread starvation under high load.', 'Annotate endpoint with @RateLimit(requests = 10, durationSeconds = 60).', '@PostMapping(\"/refresh\")\npublic ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRequest req)', '@RateLimit(limit = 10, period = 60)\n@PostMapping(\"/refresh\")\npublic ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRequest req)', 0.880, 'OPEN'),
('find-3', 'rev-1', 'pr-1', 'repo-1', 'src/main/java/com/aicode/security/RefreshTokenService.java', 52, 'Missing Unit Test for Replay Token Family Revocation', 'No test case verifies that replayed tokens trigger CompromisedTokenException and revoke child sessions.', 'LOW', 'TESTING', 'Code coverage analysis shows 0% coverage on branch if (token.isRevoked()).', 'Guarantees the core security invariant works as expected over future refactoring.', 'Undetected regression in future pull requests.', 'Add unit test testReplayTokenRevokesFamily() in RefreshTokenServiceTest.java.', NULL, '@Test\nvoid shouldRevokeFamilyOnReusedToken() {\n    Token token = Token.builder().token(\"xyz\").revoked(true).familyId(\"fam-1\").build();\n    when(tokenRepo.findByToken(\"xyz\")).thenReturn(Optional.of(token));\n    assertThrows(CompromisedTokenException.class, () -> service.verifyAndRotate(\"xyz\"));\n    verify(tokenRepo).revokeFamily(\"fam-1\");\n}', 0.960, 'OPEN')
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- Insert Notifications
INSERT INTO notifications (id, user_id, title, message, type, is_read, action_url)
VALUES
('notif-1', 'u-1', 'Review Completed for PR #42', 'Autonomous review generated 3 findings (1 High, 2 Medium) for api-gateway.', 'REVIEW_COMPLETED', FALSE, '/pull-requests/pr-1'),
('notif-2', 'u-1', 'Critical Timing Attack Vulnerability Detected', 'PR #15 in payment-service has a critical non-constant time signature validation.', 'CRITICAL_VULNERABILITY', FALSE, '/pull-requests/pr-3')
ON DUPLICATE KEY UPDATE title=VALUES(title);
