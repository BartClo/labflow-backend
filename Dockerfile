# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/labflow-backend-0.0.1-SNAPSHOT.jar app.jar

# Create startup script to convert DATABASE_URL to JDBC format
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'export SPRING_DATASOURCE_URL=$(echo $DATABASE_URL | sed "s/^postgres:/jdbc:postgresql:/")' >> /app/start.sh && \
    echo 'exec java -jar /app/app.jar' >> /app/start.sh && \
    chmod +x /app/start.sh

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["/app/start.sh"]
