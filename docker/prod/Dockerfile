## Multi-stage Dockerfile for production

# -------- Build Stage --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven descriptor and resolve dependencies first (better layer caching)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy source and build the fat jar
COPY src ./src
RUN mvn -q -DskipTests package


# -------- Runtime Stage --------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy runnable jar from builder image
COPY --from=builder /app/target/*.jar app.jar

# Copy entrypoint script that will load docker secrets into env vars
COPY docker/prod/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

# Use the entrypoint script which sets env vars from secrets and starts the app
ENTRYPOINT ["/app/entrypoint.sh"]

