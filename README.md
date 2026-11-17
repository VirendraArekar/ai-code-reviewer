# AI Code Reviewer for Pull Requests 🚀

[![CI/CD Pipeline](https://github.com/VirendraArekar/ai-code-reviewer/actions/workflows/ci.yml/badge.svg)](https://github.com/VirendraArekar/ai-code-reviewer/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 25](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-green.svg)](https://spring.io/projects/spring-boot)
[![React 19](https://img.shields.io/badge/React-19-blue.svg)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.7+-blue.svg)](https://www.typescriptlang.org/)
[![Tailwind CSS v4](https://img.shields.io/badge/Tailwind-v4-cyan.svg)](https://tailwindcss.com/)
[![MySQL 8+](https://img.shields.io/badge/MySQL-8+-blue.svg)](https://www.mysql.com/)

> Production-ready, enterprise-grade AI-powered GitHub Pull Request code review platform. Automatically analyzes code changes using LLM intelligence and static heuristics to detect bugs, security vulnerabilities, performance bottlenecks, architecture violations, code smells, and missing test cases before merging.

---

## 🌟 Key Features

- **Automated GitHub PR Reviews**: Seamless integration with GitHub Webhooks, GitHub Apps, GitHub Checks API, and inline review comments.
- **Multi-LLM Provider Abstraction**: Native support for OpenAI (GPT-4o / GPT-4o-mini), Google Gemini (Gemini 2.5/2.0 Flash & Pro), and local Ollama models (DeepSeek-Coder, Llama 3, CodeLlama).
- **Intelligent Diff Analysis & Chunking**: Syntax-aware diff parser supporting Java, TypeScript, JavaScript, Python, Go, PHP, C#, Kotlin, SQL, HTML, and CSS.
- **Deep 6-Category Review Engine**:
  - 🐛 **Bug Detection**: Null pointer exceptions, race conditions, memory leaks, logic bugs, boundary errors.
  - 🛡️ **Security Vulnerabilities**: OWASP Top 10, SQLi, XSS, CSRF, IDOR, hardcoded secrets, SSRF, path traversal.
  - ⚡ **Performance Optimization**: N+1 queries, high memory complexity, excessive loops, unmemoized renders.
  - 🧹 **Code Quality & Smells**: SOLID principles, cyclomatic complexity, DRY violations, dead code.
  - 🏛️ **Architecture & Design**: Layer violations, circular dependencies, tight coupling.
  - 🧪 **Test Coverage Analysis**: Edge cases, missing unit/integration tests, mocking suggestions.
- **Interactive Monaco Diff Viewer**: Unified and split side-by-side diff review with inline severity badges, code annotations, and one-click fixes.
- **AI PR Chat Assistant**: Contextual chatbot allowing developers to ask questions about specific pull requests, line changes, or suggested fixes.
- **Real-Time Analytics & Dashboard**: Real MySQL-driven statistics for review velocity, severity distributions, code quality trends, and team productivity.
- **Enterprise Security & Compliance**: Strict RBAC (Super Admin, Admin, Org Owner, Team Lead, Developer, Viewer), AES-256-GCM token encryption, JWT with refresh token rotation, and HMAC webhook signature verification.

---

## 📸 Application Screenshots

### ☀️ Light Mode Screens (2 screens per row)

<table>
  <tr>
    <td width="50%">
      <h4 align="center">01. Dashboard Overview</h4>
      <img src="./docs/images/01-dashboard-light.png" alt="Dashboard Overview" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">02. Connected Repositories</h4>
      <img src="./docs/images/02-repositories-light.png" alt="Connected Repositories" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">03. Pull Requests Hub</h4>
      <img src="./docs/images/03-pull-requests-light.png" alt="Pull Requests Hub" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">04. Pull Request Review & Diff</h4>
      <img src="./docs/images/03b-pull-request-detail-light.png" alt="PR Review & Diff" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">05. Findings & Vulnerabilities</h4>
      <img src="./docs/images/04-findings-light.png" alt="Findings & Vulnerabilities" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">06. Real-Time Analytics & Scoring</h4>
      <img src="./docs/images/05-analytics-light.png" alt="Real-Time Analytics & Scoring" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">07. Engineering Teams</h4>
      <img src="./docs/images/06-teams-light.png" alt="Engineering Teams" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">08. AI Provider Configurations</h4>
      <img src="./docs/images/07-ai-providers-light.png" alt="AI Provider Configurations" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">09. GitHub App & Webhooks</h4>
      <img src="./docs/images/08-github-integration-light.png" alt="GitHub App & Webhooks" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">10. Notification Center</h4>
      <img src="./docs/images/09-notifications-light.png" alt="Notification Center" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">11. Security Audit Logs</h4>
      <img src="./docs/images/10-audit-logs-light.png" alt="Security Audit Logs" width="100%" />
    </td>
    <td width="50%">
      <h4 align="center">12. Organization Settings</h4>
      <img src="./docs/images/11-organization-light.png" alt="Organization Settings" width="100%" />
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h4 align="center">13. Usage & Billing Plans</h4>
      <img src="./docs/images/12-billing-light.png" alt="Usage & Billing Plans" width="100%" />
    </td>
    <td width="50%"></td>
  </tr>
</table>

### 🌙 Dark Mode Preview (Findings Screen)

<p align="center">
  <img src="./docs/images/findings-dark.png" alt="Review Findings - Dark Mode" width="100%" />
</p>

---

## 🏗️ System Architecture

The application adopts a modular monolith architecture with strictly defined bounded contexts:

```
                      +------------------------------------------+
                      |         GitHub Developer Platform        |
                      |  (Webhooks / Checks API / Reviews / App) |
                      +--------------------+---------------------+
                                           |
                                           v
+------------------+         +----------------------------+         +---------------------+
|   React + Vite   | <=====> |    Spring Boot 3.4 / 4     | <=====> |   MySQL 8+ Database |
| Tailwind v4 + TS | (REST)  |  (Java 25 Modular Backend) |  (JPA)  | (Flyway Migrations) |
+------------------+         +--------------+-------------+         +---------------------+
                                            |
                                            v
                             +----------------------------+
                             |   AI Provider Abstraction  |
                             |  (OpenAI / Gemini / Ollama)|
                             +----------------------------+
```

---

## 💻 Tech Stack

### Frontend

- **Framework**: React 19 + TypeScript + Vite
- **State Management**: Redux Toolkit + RTK Query
- **Styling**: Tailwind CSS v4 + Lucide React + Framer Motion
- **Editor**: Monaco Editor (`@monaco-editor/react`)
- **Visualizations**: Recharts
- **Forms & Validation**: React Hook Form + Zod
- **Notifications**: Sonner

### Backend

- **Platform**: Java 25 LTS
- **Framework**: Spring Boot 3.4+ / 4.x with Spring Web, Spring Security, Spring Data JPA, Actuator
- **ORM & Database**: Hibernate 6.x + HikariCP + MySQL 8+
- **Migrations**: Flyway DB migrations
- **Mapping & Utilities**: MapStruct + Lombok + Jackson

### AI Engine

- Unified Multi-Provider Engine (OpenAI, Google Gemini, Ollama)
- Temperature, token budgeting, diff-chunking, and prompt injection defense

---

## 🚀 Quick Start (Local Development)

### Prerequisites

- Java 25+
- Node.js 20+ & npm 10+
- MySQL 8.0+
- Maven 3.9+
- Docker & Docker Compose (optional)

### 1. Database Setup

```bash
# Create MySQL Database
mysql -u root -p -e "CREATE DATABASE aicode_reviewer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 2. Backend Setup

```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

The backend API server starts at `http://localhost:8080/api/v1`.
Swagger OpenAPI documentation is available at `http://localhost:8080/swagger-ui.html`.

### 3. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The React development server launches at `http://localhost:5173`.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
