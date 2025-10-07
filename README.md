# LabFlow Backend

> Sistema de gestión de laboratorio desarrollado con Spring Boot para el manejo completo de clientes, análisis y plantillas de procedimientos.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.4-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#️-tecnologías)
- [Instalación desde Cero](#-instalación-desde-cero)
- [Configuración](#️-configuración)
- [Ejecución](#-ejecución)
- [API Endpoints](#-api-endpoints)
- [Documentación](#-documentación)
- [Estructura del Proyecto](#️-estructura-del-proyecto)
- [Desarrollo](#-desarrollo)
- [Despliegue](#-despliegue)
- [Contribuir](#-contribuir)

## 🚀 Características

- ✅ **CRUD Completo de Clientes** - Gestión integral de clientes de laboratorio
- ✅ **CRUD Completo de Análisis** - Manejo de análisis con datos complejos (JSONB, arrays)
- ✅ **CRUD Completo de Plantillas** - Plantillas de procedimientos que agrupan análisis
- ✅ **Base de Datos PostgreSQL** - Con migraciones Flyway automáticas
- ✅ **API REST Documentada** - Swagger/OpenAPI 3.0 integrado
- ✅ **Validación Robusta** - Jakarta Validation con mensajes personalizados
- ✅ **Arquitectura Moderna** - Spring Boot 3.5.0 con Java 21

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 3.5.0 | Framework principal |
| **Spring Data JPA** | - | Capa de persistencia |
| **PostgreSQL** | 17.4 | Base de datos |
| **Flyway** | - | Migraciones de BD |
| **SpringDoc OpenAPI** | 2.6.0 | Documentación API |
| **Jakarta Validation** | - | Validación de datos |
| **Maven** | 3.9.11+ | Gestión de dependencias |

## 🎯 Instalación desde Cero

### Paso 1: Instalar Java 21

#### Windows
1. Descargar [OpenJDK 21](https://adoptium.net/temurin/releases/?os=windows&arch=x64&package=jdk&version=21)
2. Ejecutar el instalador `.msi`
3. Verificar instalación:
   ```powershell
   java -version
   javac -version
   ```

#### macOS
```bash
# Usando Homebrew
brew install openjdk@21

# Configurar JAVA_HOME
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu/Debian)
```bash
# Instalar OpenJDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Verificar instalación
java -version
```

### Paso 2: Instalar PostgreSQL

#### Windows
1. Descargar [PostgreSQL 17.4](https://www.postgresql.org/download/windows/)
2. Ejecutar el instalador
3. **Recordar la contraseña del usuario `postgres`**
4. Asegurar que el servicio PostgreSQL esté ejecutándose

#### macOS
```bash
# Usando Homebrew
brew install postgresql@17
brew services start postgresql@17

# Crear usuario postgres (si no existe)
createuser -s postgres
```

#### Linux (Ubuntu/Debian)
```bash
# Instalar PostgreSQL
sudo apt update
sudo apt install postgresql postgresql-contrib

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Configurar usuario postgres
sudo -u postgres psql
\password postgres
\q
```

### Paso 3: Instalar Maven (Opcional)

#### Windows
1. Descargar [Apache Maven](https://maven.apache.org/download.cgi)
2. Extraer en `C:\Program Files\Apache\maven`
3. Agregar `C:\Program Files\Apache\maven\bin` al PATH

#### macOS/Linux
```bash
# macOS con Homebrew
brew install maven

# Ubuntu/Debian
sudo apt install maven

# Verificar instalación
mvn -version
```

> **Nota:** El proyecto incluye Maven Wrapper, por lo que Maven no es estrictamente necesario.

### Paso 4: Crear Base de Datos

```sql
-- Conectar como usuario postgres
psql -U postgres -h localhost

-- Crear base de datos y usuario
CREATE DATABASE labflow_db;
CREATE USER labflow_user WITH PASSWORD 'labflow_password';
GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;

-- Dar permisos adicionales para PostgreSQL 15+
\c labflow_db
GRANT ALL ON SCHEMA public TO labflow_user;
GRANT CREATE ON SCHEMA public TO labflow_user;

-- Salir
\q
```

### Paso 5: Clonar y Configurar Proyecto

```bash
# Clonar repositorio
git clone https://github.com/BartClo/labflow-backend.git
cd labflow-backend

# Crear archivo de configuración
cp .env.example .env
```

## ⚙️ Configuración

### Variables de Entorno (.env)

Editar el archivo `.env` con tus credenciales:

```properties
# Base de Datos PostgreSQL
DB_HOST=localhost
DB_PORT=5432
DB_NAME=labflow_db
DB_USERNAME=labflow_user
DB_PASSWORD=labflow_password

# Configuración del Servidor
SERVER_PORT=8080

# Configuración JPA/Hibernate
DDL_AUTO=validate
SHOW_SQL=false

# Flyway (Migraciones)
FLYWAY_ENABLED=true

# Logging
LOG_LEVEL=INFO
SQL_LOG_LEVEL=DEBUG

# Swagger
SWAGGER_ENABLED=true

# Perfil Spring
SPRING_PROFILES_ACTIVE=dev
```

### Verificar Conexión a Base de Datos

```bash
# Probar conexión
psql -U labflow_user -d labflow_db -h localhost -W

# Si funciona, deberías ver:
# labflow_db=>
```

## 🚀 Ejecución

### Opción 1: Con Maven Wrapper (Recomendado)

```bash
# Windows
.\mvnw.cmd clean spring-boot:run

# macOS/Linux
./mvnw clean spring-boot:run
```

### Opción 2: Con Maven Instalado

```bash
mvn clean spring-boot:run
```

### Opción 3: JAR Ejecutable

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/labflow-backend-0.0.1-SNAPSHOT.jar
```

### Verificar que Funciona

1. **API Funcionando:** http://localhost:8080/api/health
2. **Swagger UI:** http://localhost:8080/swagger-ui.html
3. **API Docs:** http://localhost:8080/v3/api-docs

Deberías ver:
```json
{
  "status": "UP",
  "timestamp": "2025-10-07T..."
}
```

## 📚 API Endpoints

### 👥 Clientes (`/api/clientes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/clientes` | Listar todos los clientes |
| `GET` | `/api/clientes/{id}` | Obtener cliente por ID |
| `GET` | `/api/clientes/buscar?nombre={nombre}` | Buscar por nombre |
| `POST` | `/api/clientes` | Crear nuevo cliente |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente |

### 🧪 Análisis (`/api/analisis`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/analisis` | Listar todos los análisis |
| `GET` | `/api/analisis/activos` | Listar solo activos |
| `GET` | `/api/analisis/{id}` | Obtener por ID |
| `GET` | `/api/analisis/codigo/{codigo}` | Obtener por código |
| `GET` | `/api/analisis/categoria/{categoria}` | Filtrar por categoría |
| `GET` | `/api/analisis/buscar?nombre={nombre}` | Buscar por nombre |
| `POST` | `/api/analisis` | Crear nuevo análisis |
| `PUT` | `/api/analisis/{id}` | Actualizar análisis |
| `DELETE` | `/api/analisis/{id}` | Eliminar análisis |
| `PATCH` | `/api/analisis/{id}/estado` | Cambiar estado |

### 📋 Plantillas (`/api/plantillas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/plantillas` | Listar todas las plantillas |
| `GET` | `/api/plantillas/activas` | Listar solo activas |
| `GET` | `/api/plantillas/{id}` | Obtener por ID |
| `GET` | `/api/plantillas/buscar?nombre={nombre}` | Buscar por nombre |
| `GET` | `/api/plantillas/tipo-muestra/{tipo}` | Filtrar por tipo de muestra |
| `POST` | `/api/plantillas` | Crear nueva plantilla |
| `PUT` | `/api/plantillas/{id}` | Actualizar plantilla |
| `DELETE` | `/api/plantillas/{id}` | Eliminar plantilla |
| `PATCH` | `/api/plantillas/{id}/estado` | Cambiar estado |
| `POST` | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Agregar análisis |
| `DELETE` | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Remover análisis |

## 📖 Documentación

### Swagger UI
Una vez iniciada la aplicación:

- **Interfaz Swagger:** http://localhost:8080/swagger-ui.html
- **Especificación OpenAPI:** http://localhost:8080/v3/api-docs

### Health Check
- **Estado de la aplicación:** http://localhost:8080/api/health

### Ejemplos de Uso

#### Crear Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Laboratorio XYZ"}'
```

#### Crear Análisis
```bash
curl -X POST http://localhost:8080/api/analisis \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Análisis de pH",
    "codigo": "PH001",
    "categoria": "Fisicoquímico",
    "descripcion": "Determinación de pH en agua",
    "metodoEnsayo": "NCh 409/1",
    "tiposMuestraAplicables": ["Agua", "Agua residual"],
    "parametrosMedir": {"pH": {"min": 0, "max": 14}},
    "equiposRequeridos": {"pHmetro": "Digital"},
    "duracionEstimadaHoras": 1.5,
    "precioClp": 15000,
    "estado": "Activo"
  }'
```

## 🏗️ Estructura del Proyecto

```
src/main/java/com/labflow/
├── 📁 config/                    # Configuración Spring
│   ├── SecurityConfig.java       # Configuración de seguridad
│   └── SwaggerConfig.java        # Configuración Swagger
├── 📁 controller/                # Controladores REST
│   ├── AnalisisController.java   # API de análisis
│   ├── ClienteController.java    # API de clientes
│   ├── HealthController.java     # Health checks
│   └── PlantillaController.java  # API de plantillas
├── 📁 dto/                       # Data Transfer Objects
│   ├── AnalisisCreateDTO.java    # DTO para crear análisis
│   ├── AnalisisDTO.java          # DTO de respuesta análisis
│   ├── ClienteCreateDTO.java     # DTO para crear cliente
│   ├── ClienteDTO.java           # DTO de respuesta cliente
│   ├── PlantillaCreateDTO.java   # DTO para crear plantilla
│   └── PlantillaDTO.java         # DTO de respuesta plantilla
├── 📁 model/                     # Entidades JPA
│   ├── Analisis.java             # Entidad análisis
│   ├── Client.java               # Entidad cliente
│   ├── Plantilla.java            # Entidad plantilla
│   └── PlantillaAnalisis.java    # Entidad relación M:N
├── 📁 repository/                # Repositorios Spring Data
│   ├── AnalisisRepository.java   # Repo análisis
│   ├── ClientRepository.java     # Repo clientes
│   ├── PlantillaRepository.java  # Repo plantillas
│   └── PlantillaAnalisisRepository.java # Repo relaciones
├── 📁 service/                   # Lógica de negocio
│   ├── AnalisisService.java      # Servicio análisis
│   ├── ClienteService.java       # Servicio clientes
│   └── PlantillaService.java     # Servicio plantillas
└── LabflowBackendApplication.java # Clase principal
```

## 🗃️ Base de Datos

### Migraciones Flyway

Las migraciones se ejecutan automáticamente:

- **V1** `clientes` - Tabla de clientes
- **V2** `analisis` - Tabla de análisis con tipos PostgreSQL avanzados
- **V3** `plantillas` - Tablas de plantillas y relaciones

### Esquema de Base de Datos

```sql
-- Clientes
CREATE TABLE clientes (
    id_cliente UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_cliente VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Análisis (simplificado)
CREATE TABLE analisis (
    id_analisis UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_analisis VARCHAR(255) NOT NULL,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    tipos_muestra_aplicables TEXT[],
    parametros_medir JSONB,
    equipos_requeridos JSONB,
    precio_clp DECIMAL(10,2),
    estado VARCHAR(20) DEFAULT 'Activo',
    -- ... más campos
);

-- Plantillas
CREATE TABLE plantillas (
    id_plantilla UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_plantilla VARCHAR(255) UNIQUE NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50) DEFAULT 'Activo',
    tipos_muestra_aplicables TEXT[],
    -- ... más campos
);
```

## 🛠️ Desarrollo

### Requisitos de Desarrollo

- Java 21+
- PostgreSQL 17.4+
- IDE con soporte Spring Boot (IntelliJ IDEA, VS Code, Eclipse)

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=ClienteServiceTest

# Con reporte de cobertura
mvn test jacoco:report
```

### Hot Reload

Para desarrollo con recarga automática:

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

### Validar Migraciones

```bash
# Estado de migraciones
mvn flyway:info

# Validar migraciones
mvn flyway:validate

# Migrar manualmente (si es necesario)
mvn flyway:migrate
```

## 🚀 Despliegue

### Variables de Entorno para Producción

```bash
# Base de datos
DB_HOST=production-db-host
DB_NAME=labflow_prod
DB_USERNAME=prod_user
DB_PASSWORD=secure_password

# Configuración
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
DDL_AUTO=validate
FLYWAY_ENABLED=true
LOG_LEVEL=WARN
```

### Docker (Recomendado)

```dockerfile
# Dockerfile
FROM openjdk:21-jdk-slim
COPY target/labflow-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# Compilar y construir imagen
mvn clean package
docker build -t labflow-backend .

# Ejecutar
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=labflow_db \
  labflow-backend
```

### JAR Tradicional

```bash
# Compilar para producción
mvn clean package -Pprod

# Ejecutar en servidor
java -jar target/labflow-backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

## 🤝 Contribuir

1. **Fork** el repositorio
2. **Crear rama** feature: `git checkout -b feature/nueva-funcionalidad`
3. **Commit** cambios: `git commit -am 'Agregar nueva funcionalidad'`
4. **Push** a la rama: `git push origin feature/nueva-funcionalidad`
5. **Crear Pull Request**

### Estándares de Código

- Seguir convenciones de Java
- Documentar APIs con OpenAPI
- Escribir tests unitarios
- Validaciones con Bean Validation
- Commits descriptivos con prefijos: `feat:`, `fix:`, `docs:`

## 🆘 Resolución de Problemas

### Error de Conexión a Base de Datos

```bash
# Verificar que PostgreSQL esté ejecutándose
# Windows
sc query postgresql-x64-17

# macOS
brew services list | grep postgresql

# Linux
sudo systemctl status postgresql
```

### Puerto 8080 en Uso

```bash
# Cambiar puerto en .env
SERVER_PORT=8081

# O matar proceso que usa el puerto
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

### Error de Autenticación PostgreSQL

```sql
-- Verificar usuario y permisos
\du labflow_user

-- Recrear usuario si es necesario
DROP USER IF EXISTS labflow_user;
CREATE USER labflow_user WITH PASSWORD 'labflow_password';
GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;
```

## 📄 Licencia

Este proyecto está bajo la [Licencia MIT](LICENSE).

## 📞 Soporte

- **Issues:** [GitHub Issues](https://github.com/BartClo/labflow-backend/issues)
- **Email:** support@labflow.com
- **Documentación:** [Wiki del Proyecto](https://github.com/BartClo/labflow-backend/wiki)

---

<div align="center">

**[⬆ Volver al inicio](#labflow-backend)**

*Desarrollado con ❤️ por el equipo LabFlow*

</div>



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