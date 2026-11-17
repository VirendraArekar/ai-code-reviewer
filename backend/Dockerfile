# ==========================================
# Stage 1: Build the application using JDK 21 LTS
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper & pom.xml first for caching dependencies
COPY pom.xml .
COPY src ./src

# Build production JAR without tests (tests run in CI stage)
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# ==========================================
# Stage 2: Runtime Image with lightweight JRE 21 LTS
# ==========================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: Create dedicated non-root application user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /build/target/ai-code-reviewer-*.jar app.jar

# Install curl for container health checks
RUN apk add --no-cache curl

# Assign ownership to appuser
RUN chown -R appuser:appgroup /app
USER appuser

# Expose backend REST API port
EXPOSE 8080

# Configure JVM options for container memory limits
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+ExitOnOutOfMemoryError"

# Container Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
