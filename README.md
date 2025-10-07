# LabFlow Backend

Sistema de gestión de laboratorio con Spring Boot que maneja clientes, análisis y plantillas de procedimientos.

## Tecnologías

- Java 21
- Spring Boot 3.5.0
- PostgreSQL 17.4
- Flyway (migraciones)
- Maven

## Funcionalidades

- CRUD completo de **Clientes**
- CRUD completo de **Análisis** de laboratorio 
- CRUD completo de **Plantillas** de procedimientos
- API REST documentada con Swagger
- Migraciones automáticas de base de datos

## Tutorial: Cómo usar el proyecto desde cero

### Prerrequisitos
- Computadora con Windows, macOS o Linux
- Conexión a internet
- Conocimientos básicos de terminal/consola

### Paso 1: Instalar Java 21

#### Windows
1. Ir a https://adoptium.net/temurin/releases/?version=21
2. Descargar el archivo `.msi` para Windows x64
3. Ejecutar el instalador y seguir los pasos
4. Abrir terminal (cmd o PowerShell) y verificar:
```bash
java -version
# Debe mostrar: openjdk version "21.x.x"
```

#### macOS
```bash
# Instalar con Homebrew
brew install openjdk@21

# Verificar instalación
java -version
```

#### Linux (Ubuntu/Debian)
```bash
# Actualizar sistema
sudo apt update

# Instalar Java 21
sudo apt install openjdk-21-jdk

# Verificar instalación
java -version
```

### Paso 2: Instalar PostgreSQL

#### Windows
1. Ir a https://www.postgresql.org/download/windows/
2. Descargar PostgreSQL 17.4
3. Ejecutar instalador
4. **IMPORTANTE:** Recordar la contraseña que pongas para el usuario `postgres`
5. Verificar que PostgreSQL esté ejecutándose en Servicios de Windows

#### macOS
```bash
# Instalar con Homebrew
brew install postgresql@17

# Iniciar servicio
brew services start postgresql@17

# Verificar instalación
psql --version
```

#### Linux (Ubuntu/Debian)
```bash
# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verificar instalación
psql --version
```

### Paso 3: Configurar Base de Datos

#### Opción A: Usando terminal/consola
```bash
# Conectar a PostgreSQL (te pedirá la contraseña)
psql -U postgres -h localhost

# Una vez conectado, ejecutar estos comandos:
CREATE DATABASE labflow_db;
CREATE USER labflow_user WITH PASSWORD 'labflow_password';
GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;
\c labflow_db
GRANT ALL ON SCHEMA public TO labflow_user;

# Salir de PostgreSQL
\q
```

#### Opción B: Usando pgAdmin (interfaz gráfica)
1. Abrir pgAdmin (se instala con PostgreSQL)
2. Conectar con usuario `postgres` y tu contraseña
3. Click derecho en "Databases" → "Create" → "Database"
4. Nombre: `labflow_db`
5. Guardar

### Paso 4: Descargar el Proyecto

```bash
# Clonar repositorio
git clone https://github.com/BartClo/labflow-backend.git

# Entrar al directorio
cd labflow-backend
```

### Paso 5: Configurar Variables de Entorno

```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar el archivo .env con tu editor preferido
# En Windows: notepad .env
# En macOS: nano .env
# En Linux: nano .env
```

Configurar estas variables en el archivo `.env`:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=labflow_db
DB_USERNAME=labflow_user
DB_PASSWORD=labflow_password
SERVER_PORT=8080
```

### Paso 6: Ejecutar la Aplicación

#### Opción A: Con Maven instalado
```bash
# Compilar y ejecutar
mvn spring-boot:run
```

#### Opción B: Con Maven Wrapper (sin instalar Maven)
```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

### Paso 7: Verificar que Funciona

1. **Abrir navegador** en: http://localhost:8080/api/health
   - Deberías ver: `{"status":"UP"}`

2. **Ver documentación API** en: http://localhost:8080/swagger-ui.html
   - Deberías ver la interfaz de Swagger con todos los endpoints

### Paso 8: Probar la API

#### Crear un cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Mi Laboratorio"}'
```

#### Listar clientes
```bash
curl http://localhost:8080/api/clientes
```

### Problemas Comunes y Soluciones

#### Error: "Puerto 8080 ya está en uso"
```bash
# Cambiar puerto en .env
SERVER_PORT=8081
```

#### Error: "No se puede conectar a PostgreSQL"
```bash
# Verificar que PostgreSQL esté ejecutándose
# Windows: Ir a Servicios y buscar PostgreSQL
# macOS: brew services list | grep postgresql
# Linux: sudo systemctl status postgresql
```

#### Error: "Base de datos no existe"
```bash
# Crear la base de datos manualmente
psql -U postgres -c "CREATE DATABASE labflow_db;"
```

### ¡Listo! 🎉

Ahora tienes el proyecto funcionando y puedes:
- Ver todos los endpoints en Swagger UI
- Crear clientes, análisis y plantillas
- Consultar la base de datos directamente

## API Endpoints

### Clientes
- `GET /api/clientes` - Listar clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes/{id}` - Obtener cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente
- `GET /api/clientes/buscar?nombre={nombre}` - Buscar por nombre

### Análisis
- `GET /api/analisis` - Listar análisis
- `GET /api/analisis/activos` - Listar activos
- `POST /api/analisis` - Crear análisis
- `GET /api/analisis/{id}` - Obtener análisis
- `PUT /api/analisis/{id}` - Actualizar análisis
- `DELETE /api/analisis/{id}` - Eliminar análisis
- `GET /api/analisis/codigo/{codigo}` - Obtener por código
- `GET /api/analisis/categoria/{categoria}` - Filtrar por categoría
- `PATCH /api/analisis/{id}/estado` - Cambiar estado

### Plantillas
- `GET /api/plantillas` - Listar plantillas
- `GET /api/plantillas/activas` - Listar activas
- `POST /api/plantillas` - Crear plantilla
- `GET /api/plantillas/{id}` - Obtener plantilla
- `PUT /api/plantillas/{id}` - Actualizar plantilla
- `DELETE /api/plantillas/{id}` - Eliminar plantilla
- `GET /api/plantillas/buscar?nombre={nombre}` - Buscar por nombre
- `PATCH /api/plantillas/{id}/estado` - Cambiar estado
- `POST /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Agregar análisis
- `DELETE /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Remover análisis

## Base de Datos

### Tablas
- `clientes` - Datos de clientes
- `analisis` - Análisis de laboratorio con JSONB y arrays
- `plantillas` - Plantillas de procedimientos
- `plantilla_analisis` - Relación M:N entre plantillas y análisis

### Migraciones Flyway (automáticas)
- V1: Tabla clientes
- V2: Tabla analisis
- V3: Tablas plantillas y plantilla_analisis

## Estructura del Proyecto

```
src/main/java/com/labflow/
├── LabflowBackendApplication.java    # Clase principal
├── config/                           # Configuración
├── controller/                       # API REST
├── dto/                              # Data Transfer Objects
├── model/                            # Entidades JPA
├── repository/                       # Repositorios Spring Data
└── service/                          # Lógica de negocio
```

## Configuración (.env)

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=labflow_db
DB_USERNAME=labflow_user
DB_PASSWORD=labflow_password
SERVER_PORT=8080
```