#!/bin/sh
# Convert Render's DATABASE_URL (postgresql://) to JDBC format (jdbc:postgresql://)
export SPRING_DATASOURCE_URL=$(echo "$DATABASE_URL" | sed 's|^postgresql://|jdbc:postgresql://|')

# Start the application
exec java $JAVA_OPTS -jar /app/app.jar
