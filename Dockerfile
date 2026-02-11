# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser && \
    chown -R appuser:appuser /app

# Instalar wget para healthcheck
RUN apk add --no-cache wget

# Copiar JAR y script desde build stage
COPY --from=build /app/target/labflow-backend-0.0.1-SNAPSHOT.jar app.jar
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh && \
    chown appuser:appuser /app/start.sh /app/app.jar

# Crear directorios para almacenamiento
RUN mkdir -p /app/reports /app/uploads && \
    chown -R appuser:appuser /app/reports /app/uploads

# Cambiar a usuario no-root
USER appuser

EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["/app/start.sh"]
