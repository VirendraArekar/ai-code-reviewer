# API Specification: AI Code Reviewer for Pull Requests

## Base URL
- Production: `https://api.aicodereviewer.com`
- Development: `http://localhost:8080`
- API Version: `v1` (`/api/v1`)

## Authentication
All secure endpoints require a Bearer token in the `Authorization` header:
```http
Authorization: Bearer <access_token>
```

---

## 1. Authentication Endpoints (`/api/v1/auth`)

### 1.1 Register New Account
- **Endpoint**: `POST /api/v1/auth/register`
- **Request Body**:
```json
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password123!",
  "organizationName": "Acme Inc"
}
```
- **Response (200 OK)**:
```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "48b6c41b-...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": "usr-123",
    "email": "jane@example.com",
    "fullName": "Jane Doe",
    "role": "ORGANIZATION_OWNER",
    "organizationId": "org-456",
    "organizationName": "Acme Inc"
  }
}
```

### 1.2 Login
- **Endpoint**: `POST /api/v1/auth/login`
- **Request Body**:
```json
{
  "email": "jane@example.com",
  "password": "Password123!"
}
```

### 1.3 Refresh Token Rotation
- **Endpoint**: `POST /api/v1/auth/refresh`
- **Request Body**:
```json
{
  "refreshToken": "48b6c41b-..."
}
```

---

## 2. Repositories Endpoints (`/api/v1/repositories`)

### 2.1 List Connected Repositories
- **Endpoint**: `GET /api/v1/repositories?orgId={orgId}&search={query}`
- **Response (200 OK)**:
```json
[
  {
    "id": "repo-001",
    "organizationId": "org-001",
    "githubRepoId": 1029384,
    "name": "core-backend",
    "fullName": "acme/core-backend",
    "ownerLogin": "acme",
    "defaultBranch": "main",
    "privateRepo": true,
    "htmlUrl": "https://github.com/acme/core-backend",
    "primaryLanguage": "Java",
    "autoReviewEnabled": true,
    "inlineCommentsEnabled": true,
    "reviewMode": "STANDARD",
    "minSeverity": "MEDIUM",
    "aiProvider": "GEMINI",
    "aiModel": "gemini-2.0-flash",
    "openPrCount": 4,
    "totalFindingsCount": 18
  }
]
```

### 2.2 Update Repository Review Configuration
- **Endpoint**: `PUT /api/v1/repositories/{id}`
- **Request Body**:
```json
{
  "autoReviewEnabled": true,
  "inlineCommentsEnabled": true,
  "reviewMode": "DEEP",
  "minSeverity": "HIGH",
  "aiProvider": "OPENAI",
  "aiModel": "gpt-4o",
  "customInstructions": "Enforce OWASP guidelines and immutable data transfer objects."
}
```

---

## 3. Pull Requests & Reviews (`/api/v1/pull-requests`, `/api/v1/reviews`)

### 3.1 List Pull Requests
- **Endpoint**: `GET /api/v1/pull-requests?repositoryId={repoId}&state={open|closed}`

### 3.2 Get Pull Request Changed Files & Diffs
- **Endpoint**: `GET /api/v1/pull-requests/{id}/files`

### 3.3 Trigger On-Demand AI Code Review
- **Endpoint**: `POST /api/v1/reviews/trigger`
- **Request Body**:
```json
{
  "pullRequestId": "pr-42",
  "reviewMode": "SECURITY_FOCUSED",
  "aiProvider": "GEMINI",
  "aiModel": "gemini-2.0-flash"
}
```

### 3.4 Interactive AI Chat for Pull Request
- **Endpoint**: `POST /api/v1/pull-requests/{id}/chat`
- **Request Body**:
```json
{
  "message": "Why is the HMAC signature check flagged as a timing attack?"
}
```
- **Response**:
```json
{
  "reply": "Standard string `.equals()` returns false immediately at the first mismatching byte. This allows attackers to guess byte-by-byte using timing measurements. Solution: Use `java.security.MessageDigest.isEqual(...)`."
}
```

---

## 4. Webhooks (`/api/v1/webhooks/github`)
- **Headers**:
  - `X-GitHub-Event`: `pull_request` | `ping`
  - `X-Hub-Signature-256`: `sha256=abcdef...`
  - `X-GitHub-Delivery`: `UUID`
- **Verification**: Evaluated using HMAC-SHA256 with the organization's stored webhook secret.
