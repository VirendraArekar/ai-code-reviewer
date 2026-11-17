# Security & Compliance Architecture

## 1. Cryptographic Safeguards

### 1.1 AES-256-GCM Encryption
All sensitive data at rest — including customer AI Provider API keys (Gemini, OpenAI), GitHub OAuth tokens, and GitHub App private keys — are encrypted using **AES-256-GCM** (Galois/Counter Mode).
- **IV Generation**: A fresh cryptographically secure random 12-byte Initialization Vector (IV) is generated for every encryption operation using `java.security.SecureRandom`.
- **Authentication Tag**: GCM generates an authenticated 128-bit authentication tag, preventing tampering or bit-flipping attacks.
- **Storage**: The IV is prepended to the ciphertext and Base64 encoded: `Base64(IV[12] + Ciphertext)`.

### 1.2 HMAC-SHA256 Webhook Verification
Incoming GitHub webhooks (`POST /api/v1/webhooks/github`) are validated against the repository or organization's stored webhook secret:
- Computes `HmacSHA256(secret, payloadBytes)`.
- Compares signatures using `java.security.MessageDigest.isEqual(...)` in constant-time to eliminate timing attack vectors.

### 1.3 JWT Authentication & Refresh Token Rotation
- **Stateless Bearer Tokens**: Signed using HMAC-SHA256 with HS256 algorithm.
- **Automatic Token Family Invalidation**: Refresh tokens belong to unique token families (`family_id`). If an already-revoked refresh token is replayed, the entire token family is immediately invalidated, terminating potential session hijacking.

---

## 2. API Security & Rate Limiting

- **Sliding-Window Rate Limiter**: Configured via `RateLimitingFilter.java` restricting unauthenticated authentication and webhook endpoints to 60 requests per minute per IP address.
- **Strict CORS Policy**: Whitelists configured frontend origin domains (`http://localhost:5173`, `http://localhost:3000`).
- **CSRF**: Disabled on stateless JWT-protected REST APIs; anti-tampering headers enforced.
- **Security Headers**: Injected via Nginx:
  - `X-Frame-Options: SAMEORIGIN`
  - `X-Content-Type-Options: nosniff`
  - `Content-Security-Policy` with trusted script and style sources.

---

## 3. Sandboxed AI Analysis & Untrusted Code

- **Zero Untrusted Code Execution**: The backend server **never** compiles, executes, or runs arbitrary pull request code. Analysis is performed strictly via AST pattern matching and semantic LLM inference on unified diffs.
- **Prompt Injection Defense**: All user inputs (pull request title, commit messages, PR descriptions, and custom instructions) are sanitized before prompt composition to prevent delimiter breaking and prompt injections.
