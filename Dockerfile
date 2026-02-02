# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/labflow-backend-0.0.1-SNAPSHOT.jar app.jar
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["/app/start.sh"]