#!/bin/sh
# Parse Render's DATABASE_URL and extract components
# Format: postgresql://user:password@host:port/database

# Extract username
export SPRING_DATASOURCE_USERNAME=$(echo "$DATABASE_URL" | sed -n 's|.*://\([^:]*\):.*|\1|p')

# Extract password
export SPRING_DATASOURCE_PASSWORD=$(echo "$DATABASE_URL" | sed -n 's|.*://[^:]*:\([^@]*\)@.*|\1|p')

# Extract host, port and database, then build JDBC URL without credentials
HOST_PORT_DB=$(echo "$DATABASE_URL" | sed -n 's|.*@\(.*\)|\1|p')
export SPRING_DATASOURCE_URL="jdbc:postgresql://$HOST_PORT_DB"

# Start the application
exec java $JAVA_OPTS -jar /app/app.jar
