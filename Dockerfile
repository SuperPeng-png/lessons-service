# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# Pre-copy pom.xml and resolve dependencies for better caching
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw \
    && ./mvnw -B dependency:go-offline

# Copy the rest of the source code
COPY src src

# Build the application
RUN ./mvnw -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

# Copy the built jar from the previous stage
COPY --from=build /workspace/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Configure the JVM and run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
