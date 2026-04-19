# ============================================================
# STAGE 1: BUILD
# Uses Maven image to compile and package the Java application
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Step 1: Copy pom.xml FIRST and download dependencies
# WHY? Docker caches this layer. If pom.xml hasn't changed,
# dependencies won't be re-downloaded on next build (saves time!)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Step 2: Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# STAGE 2: RUNTIME
# Uses a minimal JRE image (not full JDK) — much smaller!
# Builder image: ~800MB → Runtime image: ~200MB
# ============================================================
FROM eclipse-temurin:17-jre-alpine

# SECURITY: Create a non-root user to run the application
# Never run containers as root in production!
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy ONLY the built JAR from the builder stage
# (source code, Maven, build tools are NOT in this image)
COPY --from=builder /app/target/hello-service.jar app.jar

# Set file ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# HEALTHCHECK: Docker/Kubernetes can use this to check if the app is alive
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Document the port (doesn't actually publish it — informational only)
EXPOSE 8080

# Start the application
# Using exec form (JSON array) — signals are passed correctly to the JVM
ENTRYPOINT ["java", "-jar", "app.jar"]
