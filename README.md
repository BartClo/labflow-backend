# LabFlow Backend

A Spring Boot 3.5.x REST API backend for the LabFlow application with comprehensive laboratory management features.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Development](#development)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)

## Prerequisites

Before running this application, make sure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Git**

### Key Dependencies

This project uses the following main dependencies:
- **Spring Boot 3.5.0** - Main framework
- **Spring Data JPA** - Database persistence layer
- **PostgreSQL Driver** - Database connectivity
- **Spring Security** - Authentication and authorization
- **SpringDoc OpenAPI 2.6.0** - API documentation (Swagger UI)

### Java Installation

1. Download and install Java 21 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) or [OpenJDK](https://openjdk.org/projects/jdk/21/)
2. Verify installation:
   ```bash
   java -version
   ```

### Maven Installation

1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Follow the [installation guide](https://maven.apache.org/install.html)
3. Verify installation:
   ```bash
   mvn -version
   ```

### PostgreSQL Installation

1. Download and install PostgreSQL from [postgresql.org](https://www.postgresql.org/download/)
2. During installation, remember the password for the `postgres` user
3. Ensure PostgreSQL service is running
4. Create the database for the application:

#### Option 1: Using pgAdmin (Graphical Interface)
1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Right-click on "Databases" → "Create" → "Database"
4. Name: `labflow_db`
5. Click "Save"

#### Option 2: Using Command Line
```bash
# Connect to PostgreSQL
psql -U postgres -h localhost

# Create the database
CREATE DATABASE labflow_db;

# Exit
\q
```

#### Option 3: Using Provided Script
```bash
# Navigate to project directory
cd labflow-backend

# Execute the setup script
psql -U postgres -h localhost -f setup_labflow_db.sql
```

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/BartClo/labflow-backend.git
   cd labflow-backend
   ```

2. **Copy environment configuration:**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables:**
   Edit the `.env` file with your database credentials and other configurations.

4. **Install dependencies:**
   ```bash
   mvn clean install
   ```

## Configuration

### Environment Variables

The application uses the following environment variables (configure in `.env` file):

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/labflow` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `password` |
| `SERVER_PORT` | Server port | `8080` |
| `CORS_ORIGINS` | Allowed CORS origins | `http://localhost:3000` |
| `DDL_AUTO` | Hibernate DDL mode | `update` |
| `SHOW_SQL` | Show SQL queries in logs | `false` |
| `LOG_LEVEL` | Application log level | `INFO` |
| `SECURITY_LOG_LEVEL` | Security log level | `DEBUG` |

### Database Setup

1. **Create the database using one of the methods above**

2. **Update your `.env` file** with the correct database credentials:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/labflow_db
   DB_USERNAME=postgres
   DB_PASSWORD=your_postgres_password
   ```

3. **Verify database connection:**
   ```bash
   psql -U postgres -d labflow_db -h localhost
   ```

4. **The application will automatically create tables** on first run (using Hibernate DDL auto-update).

#### Database Configuration Files

The project includes database setup scripts:
- `setup_labflow_db.sql` - Complete setup with user creation
- `setup_simple.sql` - Simplified database creation only

## Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` (or the port specified in your `.env` file).

### Production Mode

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR file:**
   ```bash
   java -jar target/labflow-backend-0.0.1-SNAPSHOT.jar
   ```

### Using Maven Wrapper (if available)

```bash
# On Unix/macOS
./mvnw spring-boot:run

# On Windows
mvnw.cmd spring-boot:run
```

## API Documentation

### Swagger UI

The API documentation is automatically generated using SpringDoc OpenAPI and available at:
- **Development:** `http://localhost:8080/swagger-ui.html`
- **API Docs JSON:** `http://localhost:8080/api-docs`

### Health Check

- **GET** `/api/health` - Check if the application is running

### Base URL

- **Development:** `http://localhost:8080`
- **Production:** Your deployed URL

### Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Success message",
  "data": {},
  "timestamp": "2023-10-05T10:30:00Z"
}
```

### Error Response Format

```json
{
  "success": false,
  "message": "Error message",
  "error": "Detailed error information",
  "timestamp": "2023-10-05T10:30:00Z"
}
```

## Development

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── labflow/
│   │           ├── LabflowBackendApplication.java    # Main application class
│   │           ├── config/                          # Configuration classes
│   │           ├── controller/                      # REST controllers
│   │           ├── service/                         # Business logic
│   │           ├── repository/                      # Data access layer (JPA repositories)
│   │           ├── model/                          # Entity classes (JPA entities)
│   │           ├── dto/                            # Data Transfer Objects
│   │           │   ├── request/                    # Request DTOs
│   │           │   └── response/                   # Response DTOs
│   │           ├── exception/                      # Exception handling
│   │           └── util/                           # Utility classes
│   └── resources/
│       ├── application.properties                  # Application configuration
│       └── static/                                # Static resources
└── test/                                          # Test classes
```

### Adding New Features

1. Create entity classes in `model/` package using JPA annotations
2. Create repository interfaces in `repository/` package extending JpaRepository
3. Implement business logic in `service/` package
4. Create REST endpoints in `controller/` package with proper OpenAPI annotations
5. Add DTOs in `dto/request/` and `dto/response/` packages for API contracts
6. Handle exceptions in `exception/` package with global exception handlers
7. Add utility functions in `util/` package as needed

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add proper JavaDoc documentation for public methods
- Use OpenAPI annotations (@Operation, @ApiResponse, etc.) for API documentation
- Implement proper validation using Bean Validation annotations
- Write unit tests for new features
- Follow RESTful API design principles

## Testing

### Run all tests:
```bash
mvn test
```

### Run specific test class:
```bash
mvn test -Dtest=ClassName
```

### Generate test coverage report:
```bash
mvn jacoco:report
```

## Deployment

### Using Docker (Recommended)

1. **Create Dockerfile** (add to project root):
   ```dockerfile
   FROM openjdk:17-jdk-slim
   COPY target/labflow-backend-0.0.1-SNAPSHOT.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java","-jar","/app.jar"]
   ```

2. **Build and run:**
   ```bash
   mvn clean package
   docker build -t labflow-backend .
   docker run -p 8080:8080 labflow-backend
   ```

### Using Traditional Server

1. Build the application: `mvn clean package`
2. Copy the JAR file to your server
3. Run with: `java -jar labflow-backend-0.0.1-SNAPSHOT.jar`

## Environment Profiles

The application supports different profiles:

- **dev** - Development environment
- **prod** - Production environment
- **test** - Testing environment

Run with specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Architecture Overview

This Spring Boot application follows a layered architecture:

- **Controller Layer**: REST endpoints with OpenAPI documentation
- **Service Layer**: Business logic implementation
- **Repository Layer**: Data access using Spring Data JPA
- **Model Layer**: JPA entities representing database tables
- **DTO Layer**: Data transfer objects for API contracts
- **Configuration Layer**: Spring configuration classes
- **Exception Layer**: Global exception handling

## Troubleshooting

### Common Issues

1. **Database Connection Error:**
   - Check if PostgreSQL is running: `sudo systemctl status postgresql` (Linux) or check Services (Windows)
   - Verify database credentials in `.env` file
   - Ensure database `labflow_db` exists
   - Test connection: `psql -U postgres -d labflow_db -h localhost`

2. **Port Already in Use:**
   - Change `SERVER_PORT` in `.env` file
   - Kill process using the port: `lsof -ti:8080 | xargs kill -9` (Linux/Mac) or `netstat -ano | findstr :8080` (Windows)

3. **Maven Build Fails:**
   - Ensure Java 21 is installed and set as JAVA_HOME
   - Run `mvn clean` before `mvn install`

4. **PostgreSQL Authentication Failed:**
   - Verify username and password in `.env` file
   - Check PostgreSQL `pg_hba.conf` configuration
   - Ensure PostgreSQL allows local connections

### Logs

Application logs are available in the console. For production, configure logging in `application.properties`:

```properties
logging.file.name=logs/labflow-backend.log
logging.level.com.labflow=INFO
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit your changes: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/new-feature`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please open an issue on GitHub or contact the development team.

---

**Note:** Make sure to keep your `.env` file secure and never commit it to version control. Always use `.env.example` as a template for environment variables.