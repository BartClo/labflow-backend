# LabFlow Backend# LabFlow Backend



Sistema de gestión de laboratorio desarrollado con Spring Boot para el manejo completo de clientes, análisis y plantillas de procedimientos de laboratorio.Sistema de gestión de laboratorio desarrollado con Spring Boot para el manejo completo de clientes, análisis y plantillas de procedimientos de laboratorio.



## 🚀 Características## 🚀 Características



- **Gestión de Clientes**: CRUD completo para manejo de clientes de laboratorio- **Gestión de Clientes**: CRUD completo para manejo de clientes de laboratorio

- **Gestión de Análisis**: CRUD completo para análisis de laboratorio con datos complejos- **Gestión de Análisis**: CRUD completo para análisis de laboratorio con datos complejos

- **Gestión de Plantillas**: CRUD completo para plantillas de procedimientos que agrupan análisis- **Gestión de Plantillas**: CRUD completo para plantillas de procedimientos que agrupan análisis

- **Base de Datos**: PostgreSQL con migraciones Flyway automáticas- **Base de Datos**: PostgreSQL con migraciones Flyway automáticas

- **Documentación API**: Swagger/OpenAPI integrado y completo- **Documentación API**: Swagger/OpenAPI integrado y completo

- **Validación**: Validación robusta de datos con Jakarta Validation- **Validación**: Validación robusta de datos con Jakarta Validation

- **Arquitectura**: API REST moderna con Spring Boot 3.5.0- **Arquitectura**: API REST moderna con Spring Boot 3.5.0



## 🛠️ Tecnologías## 🛠️ Tecnologías



- **Java 21** - Lenguaje de programación principal- **Java 21** - Lenguaje de programación principal

- **Spring Boot 3.5.0** - Framework principal- **Spring Boot 3.5.0** - Framework principal

- **Spring Data JPA** - Capa de persistencia- **Spring Data JPA** - Capa de persistencia

- **PostgreSQL 17.4** - Base de datos principal- **PostgreSQL 17.4** - Base de datos principal

- **Flyway** - Migraciones de base de datos- **Flyway** - Migraciones de base de datos

- **SpringDoc OpenAPI 2.6.0** - Documentación Swagger- **SpringDoc OpenAPI 2.6.0** - Documentación Swagger

- **Jakarta Validation** - Validación de datos- **Jakarta Validation** - Validación de datos

- **Maven 3.9.11** - Gestión de dependencias- **Maven 3.9.11** - Gestión de dependencias



## 📋 Requisitos Previos## 📋 Requisitos Previos



- Java 21+- Java 21+

- PostgreSQL 17.4+- PostgreSQL 17.4+

- Maven 3.9.11+- Maven 3.9.11+



## ⚙️ Configuración## ⚙️ Configuración



### 1. Base de Datos### 1. Base de Datos



Crear la base de datos PostgreSQL:Crear la base de datos PostgreSQL:



```sql```sql

CREATE DATABASE labflow_db;CREATE DATABASE labflow_db;

CREATE USER labflow_user WITH PASSWORD 'labflow_password';CREATE USER labflow_user WITH PASSWORD 'labflow_password';

GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;

``````



### 2. Variables de Entorno### 2. Variables de Entorno



Copiar `.env.example` a `.env` y configurar:Copiar `.env.example` a `.env` y configurar:



```env```env

# Base de datos# Base de datos

DB_HOST=localhostDB_HOST=localhost

DB_PORT=5432DB_PORT=5432

DB_NAME=labflow_dbDB_NAME=labflow_db

DB_USERNAME=labflow_userDB_USERNAME=labflow_user

DB_PASSWORD=labflow_passwordDB_PASSWORD=labflow_password



# Server# Server

SERVER_PORT=8080SERVER_PORT=8080

``````



### 3. Ejecución### 3. Ejecución



```bash```bash

# Compilar# Compilar

mvn clean compilemvn clean compile



# Ejecutar# Ejecutar

mvn spring-boot:runmvn spring-boot:run



# O alternativamente con JAR# O alternativamente con JAR

mvn clean packagemvn clean package

java -jar target/labflow-backend-0.0.1-SNAPSHOT.jarjava -jar target/labflow-backend-0.0.1-SNAPSHOT.jar

``````



## 📚 API Endpoints### PostgreSQL Installation



### Clientes (`/api/clientes`)1. Download and install PostgreSQL from [postgresql.org](https://www.postgresql.org/download/)

2. During installation, remember the password for the `postgres` user

| Método | Endpoint | Descripción |3. Ensure PostgreSQL service is running

|--------|----------|-------------|4. Create the database for the application:

| GET | `/api/clientes` | Listar todos los clientes |

| GET | `/api/clientes/{id}` | Obtener cliente por ID |#### Option 1: Using pgAdmin (Graphical Interface)

| GET | `/api/clientes/buscar?nombre={nombre}` | Buscar clientes por nombre |1. Open pgAdmin

| POST | `/api/clientes` | Crear nuevo cliente |2. Connect to your PostgreSQL server

| PUT | `/api/clientes/{id}` | Actualizar cliente |3. Right-click on "Databases" → "Create" → "Database"

| DELETE | `/api/clientes/{id}` | Eliminar cliente |4. Name: `labflow_db`

5. Click "Save"

### Análisis (`/api/analisis`)

#### Option 2: Using Command Line

| Método | Endpoint | Descripción |```bash

|--------|----------|-------------|# Connect to PostgreSQL

| GET | `/api/analisis` | Listar todos los análisis |psql -U postgres -h localhost

| GET | `/api/analisis/activos` | Listar análisis activos |

| GET | `/api/analisis/{id}` | Obtener análisis por ID |# Create the database

| GET | `/api/analisis/codigo/{codigo}` | Obtener análisis por código |CREATE DATABASE labflow_db;

| GET | `/api/analisis/categoria/{categoria}` | Buscar por categoría |

| GET | `/api/analisis/buscar?nombre={nombre}` | Buscar por nombre |# Exit

| POST | `/api/analisis` | Crear nuevo análisis |\q

| PUT | `/api/analisis/{id}` | Actualizar análisis |```

| DELETE | `/api/analisis/{id}` | Eliminar análisis |

| PATCH | `/api/analisis/{id}/estado` | Cambiar estado |#### Option 3: Using Provided Script

```bash

### Plantillas (`/api/plantillas`)# Navigate to project directory

cd labflow-backend

| Método | Endpoint | Descripción |

|--------|----------|-------------|# Execute the setup script

| GET | `/api/plantillas` | Listar todas las plantillas |psql -U postgres -h localhost -f setup_labflow_db.sql

| GET | `/api/plantillas/activas` | Listar plantillas activas |```

| GET | `/api/plantillas/{id}` | Obtener plantilla por ID |

| GET | `/api/plantillas/buscar?nombre={nombre}` | Buscar por nombre |## Installation

| GET | `/api/plantillas/tipo-muestra/{tipo}` | Buscar por tipo de muestra |

| POST | `/api/plantillas` | Crear nueva plantilla |1. **Clone the repository:**

| PUT | `/api/plantillas/{id}` | Actualizar plantilla |   ```bash

| DELETE | `/api/plantillas/{id}` | Eliminar plantilla |   git clone https://github.com/BartClo/labflow-backend.git

| PATCH | `/api/plantillas/{id}/estado` | Cambiar estado |   cd labflow-backend

| POST | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Agregar análisis |   ```

| DELETE | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Remover análisis |

2. **Copy environment configuration:**

## 📖 Documentación API   ```bash

   cp .env.example .env

Una vez iniciada la aplicación, acceder a:   ```



- **Swagger UI**: http://localhost:8080/swagger-ui.html3. **Configure environment variables:**

- **OpenAPI JSON**: http://localhost:8080/v3/api-docs   Edit the `.env` file with your database credentials and other configurations.



## 🗃️ Estructura de Base de Datos4. **Install dependencies:**

   ```bash

### Tabla: clientes   mvn clean install

   ```

| Campo | Tipo | Descripción |

|-------|------|-------------|## Configuration

| id_cliente | UUID | Clave primaria |

| nombre_cliente | VARCHAR(255) | Nombre del cliente |### Environment Variables

| fecha_creacion | TIMESTAMP | Fecha de creación |

| fecha_actualizacion | TIMESTAMP | Fecha de actualización |The application uses the following environment variables (configure in `.env` file):



### Tabla: analisis| Variable | Description | Default Value |

|----------|-------------|---------------|

| Campo | Tipo | Descripción || `DB_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/labflow` |

|-------|------|-------------|| `DB_USERNAME` | Database username | `postgres` |

| id_analisis | UUID | Clave primaria || `DB_PASSWORD` | Database password | `password` |

| nombre_analisis | VARCHAR(255) | Nombre del análisis || `SERVER_PORT` | Server port | `8080` |

| codigo | VARCHAR(50) | Código único || `CORS_ORIGINS` | Allowed CORS origins | `http://localhost:3000` |

| descripcion | TEXT | Descripción detallada || `DDL_AUTO` | Hibernate DDL mode | `update` |

| categoria | VARCHAR(100) | Categoría del análisis || `SHOW_SQL` | Show SQL queries in logs | `false` |

| metodo_ensayo | VARCHAR(255) | Método de ensayo || `LOG_LEVEL` | Application log level | `INFO` |

| tipos_muestra_aplicables | TEXT[] | Array de tipos de muestra || `SECURITY_LOG_LEVEL` | Security log level | `DEBUG` |

| parametros_medir | JSONB | Parámetros en formato JSON |

| equipos_requeridos | JSONB | Equipos en formato JSON |### Database Setup

| duracion_estimada_horas | DECIMAL(5,2) | Duración en horas |

| precio_clp | DECIMAL(10,2) | Precio en pesos chilenos |1. **Create the database using one of the methods above**

| estado | VARCHAR(20) | Estado del análisis |

| fecha_creacion | TIMESTAMP | Fecha de creación |2. **Update your `.env` file** with the correct database credentials:

| fecha_actualizacion | TIMESTAMP | Fecha de actualización |   ```bash

   DB_URL=jdbc:postgresql://localhost:5432/labflow_db

### Tabla: plantillas   DB_USERNAME=postgres

   DB_PASSWORD=your_postgres_password

| Campo | Tipo | Descripción |   ```

|-------|------|-------------|

| id_plantilla | UUID | Clave primaria |3. **Verify database connection:**

| nombre_plantilla | VARCHAR(255) | Nombre de la plantilla |   ```bash

| descripcion | TEXT | Descripción detallada |   psql -U postgres -d labflow_db -h localhost

| estado | VARCHAR(50) | Estado de la plantilla |   ```

| tipos_muestra_aplicables | TEXT[] | Array de tipos de muestra |

| fecha_creacion | TIMESTAMP | Fecha de creación |4. **The application will automatically create tables** on first run (using Hibernate DDL auto-update).

| fecha_actualizacion | TIMESTAMP | Fecha de actualización |

#### Database Configuration Files

### Tabla: plantilla_analisis

The project includes database setup scripts:

| Campo | Tipo | Descripción |- `setup_labflow_db.sql` - Complete setup with user creation

|-------|------|-------------|- `setup_simple.sql` - Simplified database creation only

| id_plantilla_analisis | UUID | Clave primaria |

| id_plantilla | UUID | Foreign key a plantillas |## Running the Application

| id_analisis | UUID | Foreign key a analisis |

| orden_en_plantilla | INTEGER | Orden de ejecución |### Development Mode

| fecha_agregado | TIMESTAMP | Fecha cuando se agregó |

```bash

## 🚀 Migraciones de Base de Datosmvn spring-boot:run

```

Las migraciones se ejecutan automáticamente al iniciar la aplicación usando Flyway:

The application will start on `http://localhost:8080` (or the port specified in your `.env` file).

- **V1**: Creación de tabla `clientes`

- **V2**: Creación de tabla `analisis` con tipos avanzados PostgreSQL### Production Mode

- **V3**: Creación de tablas `plantillas` y `plantilla_analisis`

1. **Build the application:**

### Comandos Útiles de Flyway   ```bash

   mvn clean package

```bash   ```

# Ver estado de migraciones

mvn flyway:info2. **Run the JAR file:**

   ```bash

# Validar migraciones   java -jar target/labflow-backend-0.0.1-SNAPSHOT.jar

mvn flyway:validate   ```



# Ejecutar migraciones pendientes### Using Maven Wrapper (if available)

mvn flyway:migrate

``````bash

# On Unix/macOS

## 🔒 Seguridad./mvnw spring-boot:run



Para desarrollo, la seguridad está deshabilitada para facilitar el acceso a Swagger. En producción se debe configurar autenticación y autorización adecuadamente.# On Windows

mvnw.cmd spring-boot:run

## 📝 Validaciones```



### Clientes## API Documentation

- Nombre: requerido, 2-100 caracteres

### Swagger UI

### Análisis

- Nombre: requerido, máximo 255 caracteres  The API documentation is automatically generated using SpringDoc OpenAPI and available at:

- Código: requerido, único, patrón alfanumérico- **Development:** `http://localhost:8080/swagger-ui.html`

- Categoría: requerida- **API Docs JSON:** `http://localhost:8080/api-docs`

- Estado: valores permitidos (Activo, Inactivo, En Desarrollo)

- Duración: 0.1-999.99 horas### Health Check

- Precio: mínimo 0.01 CLP

- **GET** `/api/health` - Check if the application is running

### Plantillas

- Nombre: requerido, 3-255 caracteres, único### Base URL

- Estado: Activo o Inactivo

- Tipos de muestra: al menos uno requerido- **Development:** `http://localhost:8080`

- Análisis incluidos: opcional con orden configurable- **Production:** Your deployed URL



## 🏗️ Estructura del Proyecto### Response Format



```All API responses follow this format:

src/main/java/com/labflow/

├── LabflowBackendApplication.java  # Clase principal```json

├── config/                         # Configuración{

│   ├── SecurityConfig.java  "success": true,

│   └── SwaggerConfig.java  "message": "Success message",

├── controller/                     # Controladores REST  "data": {},

│   ├── AnalisisController.java  "timestamp": "2023-10-05T10:30:00Z"

│   ├── ClienteController.java}

│   ├── HealthController.java```

│   └── PlantillaController.java

├── dto/                           # Data Transfer Objects### Error Response Format

│   ├── AnalisisCreateDTO.java

│   ├── AnalisisDTO.java```json

│   ├── ClienteCreateDTO.java{

│   ├── ClienteDTO.java  "success": false,

│   ├── PlantillaCreateDTO.java  "message": "Error message",

│   └── PlantillaDTO.java  "error": "Detailed error information",

├── model/                         # Entidades JPA  "timestamp": "2023-10-05T10:30:00Z"

│   ├── Analisis.java}

│   ├── Client.java```

│   ├── Plantilla.java

│   └── PlantillaAnalisis.java## Development

├── repository/                    # Repositorios Spring Data

│   ├── AnalisisRepository.java### Project Structure

│   ├── ClientRepository.java

│   ├── PlantillaAnalisisRepository.java```

│   └── PlantillaRepository.javasrc/

└── service/                       # Lógica de negocio├── main/

    ├── AnalisisService.java│   ├── java/

    ├── ClienteService.java│   │   └── com/

    └── PlantillaService.java│   │       └── labflow/

```│   │           ├── LabflowBackendApplication.java    # Main application class

│   │           ├── config/                          # Configuration classes

## 🧪 Testing│   │           ├── controller/                      # REST controllers

│   │           ├── service/                         # Business logic

```bash│   │           ├── repository/                      # Data access layer (JPA repositories)

# Ejecutar todos los tests│   │           ├── model/                          # Entity classes (JPA entities)

mvn test│   │           ├── dto/                            # Data Transfer Objects

│   │           │   ├── request/                    # Request DTOs

# Ejecutar tests con reporte de cobertura│   │           │   └── response/                   # Response DTOs

mvn test jacoco:report│   │           ├── exception/                      # Exception handling

```│   │           └── util/                           # Utility classes

│   └── resources/

## 🚀 Deployment│       ├── application.properties                  # Application configuration

│       └── static/                                # Static resources

### Crear JAR ejecutable└── test/                                          # Test classes

```

```bash

mvn clean package### Adding New Features

java -jar target/labflow-backend-0.0.1-SNAPSHOT.jar

```1. Create entity classes in `model/` package using JPA annotations

2. Create repository interfaces in `repository/` package extending JpaRepository

### Variables de entorno para producción3. Implement business logic in `service/` package

4. Create REST endpoints in `controller/` package with proper OpenAPI annotations

```env5. Add DTOs in `dto/request/` and `dto/response/` packages for API contracts

DB_HOST=production-db-host6. Handle exceptions in `exception/` package with global exception handlers

DB_PORT=54327. Add utility functions in `util/` package as needed

DB_NAME=labflow_prod

DB_USERNAME=prod_user### Code Style

DB_PASSWORD=secure_password

SERVER_PORT=8080- Follow Java naming conventions

SPRING_PROFILES_ACTIVE=prod- Use meaningful variable and method names

```- Add proper JavaDoc documentation for public methods

- Use OpenAPI annotations (@Operation, @ApiResponse, etc.) for API documentation

## 🤝 Contribución- Implement proper validation using Bean Validation annotations

- Write unit tests for new features

1. Fork el proyecto- Follow RESTful API design principles

2. Crear rama feature (`git checkout -b feature/nueva-caracteristica`)

3. Commit cambios (`git commit -am 'Agregar nueva característica'`)## Testing

4. Push a la rama (`git push origin feature/nueva-caracteristica`)

5. Crear Pull Request### Run all tests:

```bash

## 📄 Licenciamvn test

```

Este proyecto está bajo la Licencia MIT.

### Run specific test class:

## 🔗 Enlaces```bash

mvn test -Dtest=ClassName

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)```

- [Documentación PostgreSQL](https://www.postgresql.org/docs/)

- [Documentación Flyway](https://flywaydb.org/documentation/)### Generate test coverage report:

- [Documentación Swagger](https://swagger.io/docs/)```bash
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