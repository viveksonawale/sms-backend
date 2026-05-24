# ─────────────────────────────────────────────────────────────────────────────
# Stage 1: Build
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom first (layer cache: only re-download deps on pom change)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make mvnw executable and download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copy source and build (skip tests — Render runs in CI, DB not available at build time)
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2: Run
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the fat JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Render sets PORT env var automatically; Spring Boot reads SERVER_PORT
ENV SERVER_PORT=8080

EXPOSE 8080

# --enable-preview required (used in compiler config in pom.xml)
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
