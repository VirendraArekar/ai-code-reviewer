# ==========================================
# Production Runtime Image with lightweight JRE 21 LTS
# ==========================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: Create dedicated non-root application user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy built production JAR
COPY target/ai-code-reviewer-*.jar app.jar

# Install curl for container health checks
RUN apk add --no-cache curl && chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+ExitOnOutOfMemoryError"

HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
