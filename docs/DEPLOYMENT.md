# Production Deployment Guide: AI Code Reviewer

## 1. Prerequisites
- Docker Engine 24+ & Docker Compose v2+
- MySQL 8.0+ or compatible enterprise database
- Node.js 22 (for local frontend build)
- Java 25 (for local backend compilation)
- GitHub App Credentials (Client ID, Client Secret, Private Key, Webhook Secret)
- AI Provider API Keys (Google Gemini, OpenAI, or Ollama instance)

---

## 2. Docker Compose Quickstart

Clone the repository and launch the full stack:

```bash
git clone https://github.com/VirendraArekar/ai-code-reviewer.git
cd ai-code-reviewer

# Create environment configuration
cp .env.example .env

# Start all containers in detached mode
docker compose up -d --build
```

### Checking Services Health
```bash
docker compose ps
docker compose logs -f backend
```

Once started:
- Frontend Web App: `http://localhost:3000`
- Backend REST API: `http://localhost:8080`
- Swagger UI Documentation: `http://localhost:8080/swagger-ui.html`
- MySQL Database: `localhost:3306`

---

## 3. Environment Variables Catalog

| Variable | Description | Required | Default |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile (`prod`, `dev`, `test`) | Yes | `prod` |
| `SPRING_DATASOURCE_URL` | JDBC MySQL connection URL | Yes | `jdbc:mysql://mysql:3306/aicodereviewer` |
| `SPRING_DATASOURCE_USERNAME` | Database username | Yes | `aicode_user` |
| `SPRING_DATASOURCE_PASSWORD` | Database user password | Yes | `aicode_password` |
| `APP_JWT_SECRET` | HMAC-SHA256 secret (minimum 32 bytes) | Yes | Base64 encoded key |
| `APP_SECURITY_ENCRYPTION_SECRET` | AES-256 hex key (64 hex characters) | Yes | Hex key |
| `GITHUB_APP_CLIENT_ID` | GitHub App Client ID | No | - |
| `GITHUB_APP_CLIENT_SECRET` | GitHub App Client Secret | No | - |
| `GITHUB_WEBHOOK_SECRET` | Webhook signature HMAC secret | Yes | Secret string |
| `AI_GEMINI_API_KEY` | Google Gemini API key | Optional | - |
| `AI_OPENAI_API_KEY` | OpenAI API key | Optional | - |
| `AI_OLLAMA_BASE_URL` | Ollama service base URL | Optional | `http://ollama:11434` |

---

## 4. Flyway Database Migrations

Migrations execute automatically upon backend container startup via Spring Boot Flyway integration:
- `V1__initial_schema.sql`: Creates all 17 tables, indexes, relational foreign keys, and unique constraints.
- `V2__seed_data.sql`: Provisions default organization, Super Admin credentials (`admin@aicode.com` / `AdminPassword123!`), sample repositories, and pull requests.
