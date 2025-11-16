# LabFlow Backend

Sistema de gestión de laboratorio desarrollado con Spring Boot que maneja clientes, análisis, plantillas de procedimientos y muestras con relaciones complejas.

## Tecnologías

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.0** - Framework principal
- **PostgreSQL 17.4** - Base de datos relacional
- **Flyway** - Migraciones de base de datos
- **Maven** - Gestión de dependencias
- **SpringDoc OpenAPI 2.6.0** - Documentación API
- **Spring Boot Actuator** - Monitoreo y métricas

## Funcionalidades

### Entidades Principales
- **Clientes** - Gestión completa de información de clientes
- **Análisis** - Catálogo de tipos de análisis con categorías y configuración JSONB
- **Plantillas** - Plantillas de procedimientos que agrupan múltiples análisis
- **Muestras** - Muestras con estados y relaciones complejas
- **Usuarios** - Gestión de usuarios del sistema con autenticación y control de acceso
- **Roles** - Roles del sistema con permisos granulares (Administrador, Trabajador)

### Características del Sistema
- Relaciones Many-to-Many entre muestras, análisis y plantillas
- Sistema de estados granulares para control de flujos de trabajo
- API REST completa con más de 70 endpoints documentados
- Búsquedas avanzadas con filtros múltiples
- Estadísticas del sistema y cola de trabajo
- Monitoreo con health checks y métricas
- Sistema de autenticación y control de acceso basado en roles
- Gestión de permisos granulares por rol

## Tutorial: Ejecutar el Código desde Cero

### Paso 1: Instalar Java 21

#### Windows
1. Descargar Java 21 desde: https://adoptium.net/temurin/releases/?version=21
2. Ejecutar el instalador `.msi` descargado
3. Abrir PowerShell y verificar: `java -version`

#### macOS
```bash
# Instalar con Homebrew
brew install openjdk@21

# Verificar instalación
java -version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

### Paso 2: Instalar PostgreSQL

#### Windows
1. Descargar PostgreSQL 17.4 desde: https://www.postgresql.org/download/windows/
2. Ejecutar el instalador
3. **IMPORTANTE**: Recordar la contraseña del usuario `postgres`
4. Verificar que el servicio esté ejecutándose en Servicios de Windows

#### macOS
```bash
brew install postgresql@17
brew services start postgresql@17
psql --version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
psql --version
```

### Paso 3: Configurar Base de Datos
```bash
# Conectar a PostgreSQL (introducir contraseña cuando se solicite)
psql -U postgres -h localhost

# Ejecutar estos comandos en PostgreSQL:
CREATE DATABASE labflow_db;
CREATE USER labflow_user WITH PASSWORD 'labflow_password';
GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;
\c labflow_db
GRANT ALL ON SCHEMA public TO labflow_user;
\q
```

### Paso 4: Descargar el Proyecto
```bash
# Clonar el repositorio
git clone https://github.com/BartClo/labflow-backend.git
cd labflow-backend
```

### Paso 5: Configurar Variables de Entorno
```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar el archivo .env con tu editor preferido:
# Windows: notepad .env
# macOS/Linux: nano .env
```

Configurar estas variables en `.env`:
```env
DB_URL=jdbc:postgresql://localhost:5432/labflow_db
DB_USERNAME=labflow_user
DB_PASSWORD=labflow_password
SERVER_PORT=8080
SWAGGER_ENABLED=true
LOG_LEVEL=INFO
SQL_LOG_LEVEL=DEBUG
```

### Paso 6: Ejecutar la Aplicación
```bash
# Opción A: Con Maven instalado
mvn spring-boot:run

# Opción B: Con Maven Wrapper (sin instalar Maven)
# Windows:
.\mvnw.cmd spring-boot:run

# macOS/Linux:
./mvnw spring-boot:run
```

### Paso 7: Verificar que Funciona
1. Abrir navegador en: http://localhost:8080/actuator/health
   - Debe mostrar: `{"status":"UP"}`

2. Ver documentación API en: http://localhost:8080/swagger-ui.html
   - Debe mostrar la interfaz de Swagger con todos los endpoints

### Paso 8: Probar la API
```bash
# Crear un cliente de prueba
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Laboratorio Central", "direccion": "Av. Principal 123", "telefono": "123456789", "email": "info@labcentral.com"}'

# Listar clientes
curl http://localhost:8080/api/clientes
```

### Solución de Problemas Comunes

#### Error: "Puerto 8080 ya está en uso"
```bash
# Cambiar puerto en .env
SERVER_PORT=8081
```

#### Error: "No se puede conectar a PostgreSQL"
- Windows: Verificar que el servicio PostgreSQL esté ejecutándose en Servicios
- macOS: `brew services list | grep postgresql`
- Linux: `sudo systemctl status postgresql`

#### Error: "Base de datos no existe"
```bash
# Crear la base de datos manualmente
psql -U postgres -c "CREATE DATABASE labflow_db;"
```

#### Error: "Permission denied" en scripts Maven
```bash
# Linux/macOS: Dar permisos de ejecución
chmod +x mvnw
```

## Estructura de Base de Datos

### Tablas Principales
- `clientes` - Información de clientes del laboratorio
- `analisis` - Catálogo de análisis con categorías y configuración JSONB
- `plantillas` - Plantillas de procedimientos con múltiples análisis
- `muestras` - Muestras de laboratorio con estados y metadatos
- `roles` - Roles del sistema con permisos (ADMINISTRADOR, TRABAJADOR)
- `usuarios` - Usuarios del sistema con autenticación y datos personales

### Tablas de Relación
- `plantilla_analisis` - Relación M:N entre plantillas y análisis
- `muestra_analisis` - Relación M:N entre muestras y análisis individuales
- `muestra_plantilla` - Relación M:N entre muestras y plantillas completas

### Migraciones Flyway
- V1: Tabla clientes con validaciones básicas
- V2: Tabla analisis con JSONB y arrays PostgreSQL
- V3: Tablas plantillas y plantilla_analisis con relaciones M:N
- V4: Tablas muestras, muestra_analisis y muestra_plantilla con estados avanzados
- V5: Tablas usuarios y roles con autenticación y control de acceso

## API REST

### Endpoints Principales

#### Clientes
- `GET /api/clientes` - Listar clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

#### Análisis
- `GET /api/analisis` - Listar todos los análisis
- `GET /api/analisis/activos` - Listar análisis activos
- `POST /api/analisis` - Crear análisis
- `GET /api/analisis/{id}` - Obtener análisis por ID
- `GET /api/analisis/codigo/{codigo}` - Buscar por código
- `PATCH /api/analisis/{id}/estado` - Cambiar estado

#### Plantillas
- `GET /api/plantillas` - Listar plantillas
- `POST /api/plantillas` - Crear plantilla
- `GET /api/plantillas/{id}` - Obtener plantilla por ID
- `POST /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Agregar análisis
- `DELETE /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Remover análisis

#### Muestras - CRUD
- `GET /api/muestras` - Listar muestras (paginado)
- `POST /api/muestras` - Crear nueva muestra
- `GET /api/muestras/{id}` - Obtener muestra por ID
- `PUT /api/muestras/{id}` - Actualizar muestra
- `DELETE /api/muestras/{id}` - Eliminar muestra

#### Muestras - Búsquedas
- `GET /api/muestras/buscar` - Búsqueda con múltiples criterios
- `GET /api/muestras/buscar-texto?texto={texto}` - Búsqueda de texto libre
- `GET /api/muestras/cliente/{clienteId}` - Muestras de un cliente

#### Muestras - Estados
- `PATCH /api/muestras/{id}/estado?estado={estado}` - Cambiar estado
- `POST /api/muestras/{id}/recibir` - Marcar como recibida
- `POST /api/muestras/{id}/procesar` - Iniciar procesamiento
- `POST /api/muestras/{id}/completar` - Completar muestra

#### Muestras - Análisis y Plantillas
- `POST /api/muestras/{muestraId}/analisis/{analisisId}` - Asignar análisis
- `DELETE /api/muestras/{muestraId}/analisis/{analisisId}` - Remover análisis
- `POST /api/muestras/{muestraId}/plantillas/{plantillaId}` - Asignar plantilla
- `GET /api/muestras/{id}/analisis` - Ver análisis asignados
- `GET /api/muestras/{id}/plantillas` - Ver plantillas asignadas

#### Sistema
- `GET /api/muestras/estadisticas` - Estadísticas del sistema
- `GET /api/muestras/cola-laboratorio` - Cola de trabajo
- `GET /actuator/health` - Estado de salud
- `GET /actuator/metrics` - Métricas del sistema

#### Roles
- `GET /api/roles` - Listar todos los roles
- `GET /api/roles/activos` - Listar roles activos
- `POST /api/roles` - Crear nuevo rol
- `GET /api/roles/{id}` - Obtener rol por ID
- `GET /api/roles/nombre/{nombre}` - Obtener rol por nombre
- `PUT /api/roles/{id}` - Actualizar rol
- `PATCH /api/roles/{id}/estado` - Cambiar estado de rol
- `DELETE /api/roles/{id}` - Eliminar rol
- `GET /api/roles/buscar` - Buscar roles por texto

#### Usuarios
- `GET /api/usuarios` - Listar usuarios (con paginación opcional)
- `GET /api/usuarios/activos` - Listar usuarios activos
- `POST /api/usuarios` - Crear nuevo usuario
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/email/{email}` - Obtener usuario por email
- `GET /api/usuarios/username/{username}` - Obtener usuario por username
- `GET /api/usuarios/rol/{rolId}` - Listar usuarios por rol
- `GET /api/usuarios/administradores` - Listar administradores
- `GET /api/usuarios/trabajadores` - Listar trabajadores
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `PATCH /api/usuarios/{id}/estado` - Cambiar estado de usuario
- `POST /api/usuarios/{id}/registrar-conexion` - Registrar conexión
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `GET /api/usuarios/buscar` - Buscar usuarios por texto
- `GET /api/usuarios/estadisticas` - Estadísticas de usuarios

## Estructura del Proyecto

```
src/main/java/com/labflow/
├── LabflowBackendApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AnalisisController.java
│   ├── ClienteController.java
│   ├── MuestraController.java
│   ├── PlantillaController.java
│   ├── RolController.java
│   └── UsuarioController.java
├── dto/
│   ├── MuestraCreateDTO.java
│   ├── MuestraDTO.java
│   ├── RolCreateDTO.java
│   ├── RolDTO.java
│   ├── UsuarioCreateDTO.java
│   └── UsuarioDTO.java
├── exception/
├── model/
│   ├── Analisis.java
│   ├── Client.java
│   ├── Muestra.java
│   ├── MuestraAnalisis.java
│   ├── MuestraPlantilla.java
│   ├── Plantilla.java
│   ├── PlantillaAnalisis.java
│   ├── Rol.java
│   └── Usuario.java
├── repository/
│   ├── RolRepository.java
│   └── UsuarioRepository.java
├── service/
│   ├── RolService.java
│   └── UsuarioService.java
└── util/

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__Create_clientes_table.sql
    ├── V2__Create_analisis_table.sql
    ├── V3__Create_plantillas_table.sql
    ├── V4__Create_muestras_table.sql
    └── V5__Create_usuarios_roles_tables.sql
```

## Comandos de Desarrollo

```bash
# Compilar proyecto
mvn clean compile

# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar tests
mvn test

# Crear JAR
mvn clean package

# Ver estado de migraciones
mvn flyway:info

# Ejecutar migraciones
mvn flyway:migrate
```

## Sistema de Estados

### Estados de Muestra
```
REGISTRADA → RECIBIDA → EN_PROCESO → COMPLETADA
                                  ↘ RECHAZADA
```

### Estados de Análisis
```
PENDIENTE → EN_PROCESO → COMPLETADO
                      ↘ RECHAZADO
```

### Estados de Plantilla
```
ASIGNADA → EN_PROGRESO → COMPLETADA
                       ↘ CANCELADA
```

## Configuración

### Variables de Entorno (.env)
```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/labflow_db
DB_USERNAME=labflow_user
DB_PASSWORD=labflow_password

# Server Configuration
SERVER_PORT=8080

# Application Configuration
SWAGGER_ENABLED=true
LOG_LEVEL=INFO
SQL_LOG_LEVEL=DEBUG
```

## URLs del Sistema

- Aplicación: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
- Health Check: http://localhost:8080/actuator/health
- Métricas: http://localhost:8080/actuator/metrics

## Notas Técnicas

- Configuración actual optimizada para desarrollo (sin autenticación)
- CORS habilitado para desarrollo
- CSRF deshabilitado para facilitar pruebas
- Migraciones automáticas con Flyway
- Validación automática con Bean Validation
- Documentación automática con SpringDoc OpenAPI

## Desarrollo

El proyecto está configurado para desarrollo con:
- Hot reload habilitado con DevTools
- SQL logging visible en desarrollo
- Swagger integrado para documentación en tiempo real
- Actuator para monitoreo y métricas
- Seguridad deshabilitada para facilitar desarrollo