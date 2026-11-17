# User Guide: AI Code Reviewer for Pull Requests

## 1. Getting Started

### 1.1 First-Time Login
1. Open the application at `http://localhost:3000` (or your deployed URL).
2. Log in using the default Super Admin credentials:
   - **Email**: `admin@aicode.com`
   - **Password**: `AdminPassword123!`
3. Or click **Create Account** to register a new organization and workspace.

### 1.2 Onboarding Flow
The interactive 3-step onboarding wizard guides you through:
1. **Connect GitHub**: Authorize the GitHub App or connect your personal/organization access token.
2. **Select Repositories**: Choose which repositories you want to enable for automatic code reviews.
3. **Configure AI Provider**: Select your preferred AI Engine (Google Gemini, OpenAI, or local Ollama) and review mode.

---

## 2. Review Modes & Configuration

AI Code Reviewer supports 7 tailored review modes:
- **Quick Review**: Fast turnaround check focusing on critical bugs, syntax mistakes, and obvious regressions.
- **Standard Review**: Balanced analysis across all 6 core categories.
- **Deep Review**: Comprehensive line-by-line inspection with architectural analysis.
- **Security-Focused Review**: Audits for OWASP Top 10 vulnerabilities, timing attacks, SQL injection, hardcoded secrets, and cryptographic weaknesses.
- **Performance-Focused Review**: Detects N+1 database queries, high memory complexity, excessive loops, and unoptimized React re-renders.
- **Architecture Review**: Identifies tight coupling, circular dependencies, and SOLID violations.
- **Testing Review**: Checks for missing unit test coverage and unverified edge cases in changed code paths.

---

## 3. Reviewing Findings & Interactive Chat

### 3.1 Review Results View
Each Pull Request review contains:
- **Quality & Security Score Gauges**: Visual circular meters rating your code health from 0 to 100.
- **Executive Summary**: AI-generated summary of the changeset.
- **Unified Diff Viewer**: Side-by-side or inline code diff powered by Monaco Editor.
- **Categorized Findings**:
  - Exact file and changed line numbers.
  - Severity level: `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`, `INFO`.
  - Category: `BUG`, `SECURITY`, `PERFORMANCE`, `CODE_QUALITY`, `ARCHITECTURE`, `TESTING`.
  - Explanation: Why it matters and potential production impact.
  - Recommended Fix & Corrected Code snippet.

### 3.2 Interactive AI PR Assistant
Ask questions directly in the Pull Request Assistant drawer:
- *"Explain why line 45 has a race condition."*
- *"Generate a unit test verifying this edge case."*
- *"Can this SQL query cause high latency on large tables?"*
The AI assistant maintains the PR diff in context and provides instant, actionable technical explanations.
