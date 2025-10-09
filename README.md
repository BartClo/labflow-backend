# LabFlow Backend# LabFlow Backend



Sistema completo de gestión de laboratorio desarrollado con Spring Boot que maneja clientes, análisis, plantillas de procedimientos y muestras con relaciones complejas.Sistema completo de gestión de laboratorio con Spring Boot que maneja clientes, análisis, plantillas de procedimientos y muestras con sus relaciones complejas.



## 🚀 Características Principales## Tecnologías



### Gestión Completa de Entidades- **Java 21** - Lenguaje de programación

- ✅ **CRUD de Clientes** - Gestión completa de información de clientes- **Spring Boot 3.5.0** - Framework principal

- ✅ **CRUD de Análisis** - Catálogo de análisis con categorías y configuración JSONB- **PostgreSQL 17.4** - Base de datos

- ✅ **CRUD de Plantillas** - Plantillas de procedimientos que agrupan múltiples análisis- **Flyway** - Migraciones de base de datos

- ✅ **CRUD de Muestras** - Sistema completo con estados y relaciones complejas- **Maven** - Gestión de dependencias

- **Spring Security** - Seguridad (configurada para desarrollo)

### Relaciones Avanzadas- **SpringDoc OpenAPI 2.6.0** - Documentación API (Swagger)

- 🔗 **Muestras ↔ Análisis Individuales** - Relación M:N con estados independientes- **Spring Boot Actuator** - Monitoreo y métricas

- 🔗 **Muestras ↔ Plantillas de Análisis** - Relación M:N con estados y orden de ejecución

- 🔗 **Plantillas ↔ Análisis** - Agrupación de análisis en plantillas reutilizables## Funcionalidades Principales



### Funcionalidades Operacionales### Gestión de Entidades

- 📊 **Estados Granulares** - Control detallado de flujos de trabajo- ✅ **CRUD completo de Clientes** - Gestión de información de clientes

- 🔍 **Búsquedas Avanzadas** - Filtros múltiples y búsqueda de texto libre- ✅ **CRUD completo de Análisis** - Tipos de análisis de laboratorio con categorías

- 📈 **Estadísticas Completas** - Métricas detalladas del laboratorio- ✅ **CRUD completo de Plantillas** - Plantillas de procedimientos con múltiples análisis

- ⚡ **Cola de Trabajo** - Sistema de priorización y gestión de cargas- ✅ **CRUD completo de Muestras** - Muestras con estados y relaciones complejas

- 📋 **API REST Documentada** - Swagger/OpenAPI integrado

- 🏥 **Monitoreo** - Health checks y métricas con Actuator### Relaciones Avanzadas

- ✅ **Muestras ↔ Análisis Individuales** - Relación M:N con estados independientes

## 🛠️ Tecnologías- ✅ **Muestras ↔ Plantillas de Análisis** - Relación M:N con estados y orden de ejecución

- ✅ **Plantillas ↔ Análisis** - Agrupación de análisis en plantillas reutilizables

- **Java 21** - Lenguaje de programación

- **Spring Boot 3.5.0** - Framework principal### Funcionalidades Operacionales

- **PostgreSQL 17.4** - Base de datos relacional- ✅ **Gestión de Estados** - Estados granulares para muestras, análisis y plantillas

- **Flyway** - Migraciones de base de datos- ✅ **Cola de Trabajo** - Sistema de priorización y gestión de cargas de trabajo

- **Maven** - Gestión de dependencias- ✅ **Estadísticas Completas** - Métricas detalladas del laboratorio

- **Spring Security** - Seguridad (configurada para desarrollo)- ✅ **Búsquedas Avanzadas** - Filtros múltiples y búsqueda de texto libre

- **SpringDoc OpenAPI 2.6.0** - Documentación API (Swagger)- ✅ **API REST completa** - Documentada con Swagger/OpenAPI

- **Spring Boot Actuator** - Monitoreo y métricas- ✅ **Monitoreo** - Health checks y métricas con Actuator



## 📋 Prerrequisitos## Tutorial: Cómo usar el proyecto desde cero



- **Java 21** instalado### Prerrequisitos

- **PostgreSQL 17.4** ejecutándose- Computadora con Windows, macOS o Linux

- **Maven 3.8+** (opcional si usas el wrapper)- Conexión a internet

- **Git** para clonar el repositorio- Conocimientos básicos de terminal/consola



## 🚀 Guía de Instalación Rápida### Paso 1: Instalar Java 21



### 1. Clonar el Repositorio#### Windows

```bash1. Ir a https://adoptium.net/temurin/releases/?version=21

git clone https://github.com/BartClo/labflow-backend.git2. Descargar el archivo `.msi` para Windows x64

cd labflow-backend3. Ejecutar el instalador y seguir los pasos

```4. Abrir terminal (cmd o PowerShell) y verificar:

```bash

### 2. Configurar Base de Datosjava -version

```sql# Debe mostrar: openjdk version "21.x.x"

-- Conectar a PostgreSQL como superusuario```

psql -U postgres

#### macOS

-- Crear base de datos y usuario```bash

CREATE DATABASE labflow_db;# Instalar con Homebrew

CREATE USER labflow_user WITH PASSWORD 'labflow_password';brew install openjdk@21

GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;

\\c labflow_db# Verificar instalación

GRANT ALL ON SCHEMA public TO labflow_user;java -version

``````



### 3. Configurar Variables de Entorno#### Linux (Ubuntu/Debian)

```bash```bash

# Copiar archivo de ejemplo# Actualizar sistema

cp .env.example .envsudo apt update



# Editar variables según tu configuración# Instalar Java 21

# DB_URL=jdbc:postgresql://localhost:5432/labflow_dbsudo apt install openjdk-21-jdk

# DB_USERNAME=labflow_user

# DB_PASSWORD=labflow_password# Verificar instalación

```java -version

```

### 4. Ejecutar la Aplicación

```bash### Paso 2: Instalar PostgreSQL

# Con Maven instalado

mvn spring-boot:run#### Windows

1. Ir a https://www.postgresql.org/download/windows/

# Con Maven Wrapper2. Descargar PostgreSQL 17.4

./mvnw spring-boot:run  # Linux/macOS3. Ejecutar instalador

.\\mvnw.cmd spring-boot:run  # Windows4. **IMPORTANTE:** Recordar la contraseña que pongas para el usuario `postgres`

```5. Verificar que PostgreSQL esté ejecutándose en Servicios de Windows



### 5. Verificar Funcionamiento#### macOS

- **Health Check**: http://localhost:8080/actuator/health```bash

- **Swagger UI**: http://localhost:8080/swagger-ui.html# Instalar con Homebrew

- **API Docs**: http://localhost:8080/v3/api-docsbrew install postgresql@17



## 📖 API Endpoints# Iniciar servicio

brew services start postgresql@17

### 👥 Clientes

| Método | Endpoint | Descripción |# Verificar instalación

|--------|----------|-------------|psql --version

| GET | `/api/clientes` | Listar todos los clientes |```

| POST | `/api/clientes` | Crear nuevo cliente |

| GET | `/api/clientes/{id}` | Obtener cliente por ID |#### Linux (Ubuntu/Debian)

| PUT | `/api/clientes/{id}` | Actualizar cliente |```bash

| DELETE | `/api/clientes/{id}` | Eliminar cliente |# Instalar PostgreSQL

| GET | `/api/clientes/buscar?nombre={nombre}` | Buscar por nombre |sudo apt install postgresql postgresql-contrib



### 🔬 Análisis# Iniciar servicio

| Método | Endpoint | Descripción |sudo systemctl start postgresql

|--------|----------|-------------|sudo systemctl enable postgresql

| GET | `/api/analisis` | Listar todos los análisis |

| GET | `/api/analisis/activos` | Listar análisis activos |# Verificar instalación

| POST | `/api/analisis` | Crear nuevo análisis |psql --version

| GET | `/api/analisis/{id}` | Obtener análisis por ID |```

| PUT | `/api/analisis/{id}` | Actualizar análisis |

| DELETE | `/api/analisis/{id}` | Eliminar análisis |### Paso 3: Configurar Base de Datos

| GET | `/api/analisis/codigo/{codigo}` | Buscar por código |

| GET | `/api/analisis/categoria/{categoria}` | Filtrar por categoría |#### Opción A: Usando terminal/consola

| PATCH | `/api/analisis/{id}/estado` | Cambiar estado |```bash

# Conectar a PostgreSQL (te pedirá la contraseña)

### 📋 Plantillaspsql -U postgres -h localhost

| Método | Endpoint | Descripción |

|--------|----------|-------------|# Una vez conectado, ejecutar estos comandos:

| GET | `/api/plantillas` | Listar todas las plantillas |CREATE DATABASE labflow_db;

| GET | `/api/plantillas/activas` | Listar plantillas activas |CREATE USER labflow_user WITH PASSWORD 'labflow_password';

| POST | `/api/plantillas` | Crear nueva plantilla |GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;

| GET | `/api/plantillas/{id}` | Obtener plantilla por ID |\c labflow_db

| PUT | `/api/plantillas/{id}` | Actualizar plantilla |GRANT ALL ON SCHEMA public TO labflow_user;

| DELETE | `/api/plantillas/{id}` | Eliminar plantilla |

| POST | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Agregar análisis |# Salir de PostgreSQL

| DELETE | `/api/plantillas/{idPlantilla}/analisis/{idAnalisis}` | Remover análisis |\q

```

### 🧪 Muestras - CRUD Básico

| Método | Endpoint | Descripción |#### Opción B: Usando pgAdmin (interfaz gráfica)

|--------|----------|-------------|1. Abrir pgAdmin (se instala con PostgreSQL)

| GET | `/api/muestras` | Listar muestras (paginado) |2. Conectar con usuario `postgres` y tu contraseña

| POST | `/api/muestras` | Crear nueva muestra |3. Click derecho en "Databases" → "Create" → "Database"

| GET | `/api/muestras/{id}` | Obtener muestra por ID |4. Nombre: `labflow_db`

| PUT | `/api/muestras/{id}` | Actualizar muestra |5. Guardar

| DELETE | `/api/muestras/{id}` | Eliminar muestra |

| GET | `/api/muestras/numero-interno/{numero}` | Buscar por número interno |### Paso 4: Descargar el Proyecto



### 🔍 Muestras - Búsquedas y Filtros```bash

| Método | Endpoint | Descripción |# Clonar repositorio

|--------|----------|-------------|git clone https://github.com/BartClo/labflow-backend.git

| GET | `/api/muestras/buscar` | Búsqueda con múltiples criterios |

| GET | `/api/muestras/buscar-texto?texto={texto}` | Búsqueda de texto libre |# Entrar al directorio

| GET | `/api/muestras/cliente/{clienteId}` | Muestras de un cliente |cd labflow-backend

```

### ⚡ Muestras - Gestión de Estados

| Método | Endpoint | Descripción |### Paso 5: Configurar Variables de Entorno

|--------|----------|-------------|

| PATCH | `/api/muestras/{id}/estado?estado={estado}` | Cambiar estado general |```bash

| POST | `/api/muestras/{id}/recibir` | Marcar como recibida |# Copiar archivo de ejemplo

| POST | `/api/muestras/{id}/procesar` | Iniciar procesamiento |cp .env.example .env

| POST | `/api/muestras/{id}/completar` | Marcar como completada |

| POST | `/api/muestras/{id}/rechazar` | Rechazar muestra |# Editar el archivo .env con tu editor preferido

# En Windows: notepad .env

### 🔬 Muestras - Análisis Individuales# En macOS: nano .env

| Método | Endpoint | Descripción |# En Linux: nano .env

|--------|----------|-------------|```

| POST | `/api/muestras/{muestraId}/analisis/{analisisId}` | Asignar análisis |

| DELETE | `/api/muestras/{muestraId}/analisis/{analisisId}` | Remover análisis |Configurar estas variables en el archivo `.env`:

| GET | `/api/muestras/{id}/analisis` | Ver análisis asignados |```env

| PATCH | `/api/muestras/{muestraId}/analisis/{analisisId}/estado` | Cambiar estado |# Database Configuration

DB_URL=jdbc:postgresql://localhost:5432/labflow_db

### 📋 Muestras - Plantillas CompletasDB_USERNAME=labflow_user

| Método | Endpoint | Descripción |DB_PASSWORD=labflow_password

|--------|----------|-------------|

| POST | `/api/muestras/{muestraId}/plantillas/{plantillaId}` | Asignar plantilla |# Server Configuration

| DELETE | `/api/muestras/{muestraId}/plantillas/{plantillaId}` | Remover plantilla |SERVER_PORT=8080

| GET | `/api/muestras/{id}/plantillas` | Ver plantillas asignadas |

| PATCH | `/api/muestras/{muestraId}/plantillas/{plantillaId}/estado` | Cambiar estado |# Swagger Configuration

SWAGGER_ENABLED=true

### 📊 Estadísticas y Monitoreo

| Método | Endpoint | Descripción |# Logging Configuration

|--------|----------|-------------|LOG_LEVEL=INFO

| GET | `/api/muestras/estadisticas` | Estadísticas del sistema |SQL_LOG_LEVEL=DEBUG

| GET | `/api/muestras/cola-laboratorio` | Cola de trabajo |```

| GET | `/actuator/health` | Estado de salud |

| GET | `/actuator/metrics` | Métricas del sistema |### Paso 6: Ejecutar la Aplicación



## 🗄️ Base de Datos#### Opción A: Con Maven instalado

```bash

### Esquema Actual (Versión 4)# Compilar y ejecutar

El sistema utiliza PostgreSQL 17.4 con migraciones automáticas vía Flyway.mvn spring-boot:run

```

### Tablas Principales

- **`clientes`** - Información de clientes del laboratorio#### Opción B: Con Maven Wrapper (sin instalar Maven)

- **`analisis`** - Catálogo de análisis con categorías y configuración JSONB```bash

- **`plantillas`** - Plantillas de procedimientos que agrupan múltiples análisis# Windows

- **`muestras`** - Muestras de laboratorio con estados y metadatos completos.\mvnw.cmd spring-boot:run



### Tablas de Relación (Many-to-Many)# macOS/Linux

- **`plantilla_analisis`** - Relación entre plantillas y análisis./mvnw spring-boot:run

- **`muestra_analisis`** - Relación entre muestras y análisis (con estados)```

- **`muestra_plantilla`** - Relación entre muestras y plantillas (con estados y orden)

### Paso 7: Verificar que Funciona

### Características Avanzadas

- ✅ **Campos JSONB** - Configuraciones flexibles en análisis1. **Abrir navegador** en: http://localhost:8080/actuator/health

- ✅ **Arrays PostgreSQL** - Almacenamiento de listas de elementos   - Deberías ver: `{"status":"UP"}`

- ✅ **Estados granulares** - Control detallado de flujos de trabajo

- ✅ **Triggers y funciones** - Automación de procesos2. **Ver documentación API** en: http://localhost:8080/swagger-ui.html

- ✅ **Índices optimizados** - Búsquedas eficientes   - Deberías ver la interfaz de Swagger con todos los endpoints

- ✅ **Constraints complejos** - Integridad de datos

### Paso 8: Probar la API

### Migraciones Flyway

- **V1** - Tabla `clientes` con validaciones básicas#### Crear un cliente

- **V2** - Tabla `analisis` con JSONB y arrays```bash

- **V3** - Tablas `plantillas` y `plantilla_analisis` con relaciones M:Ncurl -X POST http://localhost:8080/api/clientes \

- **V4** - Tablas `muestras`, `muestra_analisis` y `muestra_plantilla` con estados avanzados  -H "Content-Type: application/json" \

  -d '{"nombre": "Mi Laboratorio"}'

## 📁 Estructura del Proyecto```



```#### Listar clientes

src/main/java/com/labflow/```bash

├── LabflowBackendApplication.java    # Clase principal Spring Bootcurl http://localhost:8080/api/clientes

├── config/```

│   └── SecurityConfig.java          # Configuración de seguridad

├── controller/                       # Controladores REST### Problemas Comunes y Soluciones

│   ├── AnalisisController.java      # API de análisis

│   ├── ClienteController.java       # API de clientes#### Error: "Puerto 8080 ya está en uso"

│   ├── HealthController.java        # Health checks personalizados```bash

│   ├── MuestraController.java       # API completa de muestras# Cambiar puerto en .env

│   └── PlantillaController.java     # API de plantillasSERVER_PORT=8081

├── dto/                             # Data Transfer Objects```

│   ├── MuestraCreateDTO.java        # DTO para creación de muestras

│   └── MuestraDTO.java              # DTO de respuesta de muestras#### Error: "No se puede conectar a PostgreSQL"

├── exception/                       # Manejo de excepciones```bash

├── model/                           # Entidades JPA# Verificar que PostgreSQL esté ejecutándose

│   ├── Analisis.java               # Entidad análisis# Windows: Ir a Servicios y buscar PostgreSQL

│   ├── Client.java                 # Entidad cliente# macOS: brew services list | grep postgresql

│   ├── Muestra.java                # Entidad muestra# Linux: sudo systemctl status postgresql

│   ├── MuestraAnalisis.java        # Relación muestra-análisis```

│   ├── MuestraPlantilla.java       # Relación muestra-plantilla

│   ├── Plantilla.java              # Entidad plantilla#### Error: "Base de datos no existe"

│   └── PlantillaAnalisis.java      # Relación plantilla-análisis```bash

├── repository/                      # Repositorios Spring Data# Crear la base de datos manualmente

│   ├── MuestraRepository.java      # Consultas de muestraspsql -U postgres -c "CREATE DATABASE labflow_db;"

│   ├── MuestraAnalisisRepository.java # Consultas muestra-análisis```

│   └── MuestraPlantillaRepository.java # Consultas muestra-plantilla

├── service/                         # Lógica de negocio### ¡Listo! 🎉

│   └── MuestraService.java         # Servicio completo de muestras

└── util/                           # UtilidadesAhora tienes el proyecto funcionando y puedes:

- Ver todos los endpoints en Swagger UI

src/main/resources/- Crear clientes, análisis y plantillas

├── application.properties           # Configuración principal- Consultar la base de datos directamente

└── db/migration/                   # Migraciones Flyway

    ├── V1__Create_clientes_table.sql## API Endpoints

    ├── V2__Create_analisis_table.sql

    ├── V3__Create_plantillas_table.sql### Clientes

    └── V4__Create_muestras_table.sql- `GET /api/clientes` - Listar clientes

```- `POST /api/clientes` - Crear cliente

- `GET /api/clientes/{id}` - Obtener cliente por ID

## ⚙️ Configuración- `PUT /api/clientes/{id}` - Actualizar cliente

- `DELETE /api/clientes/{id}` - Eliminar cliente

### Variables de Entorno (.env)- `GET /api/clientes/buscar?nombre={nombre}` - Buscar por nombre

```env

# Configuración de Base de Datos### Análisis

DB_URL=jdbc:postgresql://localhost:5432/labflow_db- `GET /api/analisis` - Listar análisis

DB_USERNAME=labflow_user- `GET /api/analisis/activos` - Listar análisis activos

DB_PASSWORD=labflow_password- `POST /api/analisis` - Crear análisis

- `GET /api/analisis/{id}` - Obtener análisis por ID

# Configuración del Servidor- `PUT /api/analisis/{id}` - Actualizar análisis

SERVER_PORT=8080- `DELETE /api/analisis/{id}` - Eliminar análisis

- `GET /api/analisis/codigo/{codigo}` - Obtener por código

# Configuración de Swagger- `GET /api/analisis/categoria/{categoria}` - Filtrar por categoría

SWAGGER_ENABLED=true- `PATCH /api/analisis/{id}/estado` - Cambiar estado



# Configuración de Logging### Plantillas

LOG_LEVEL=INFO- `GET /api/plantillas` - Listar plantillas

SQL_LOG_LEVEL=DEBUG- `GET /api/plantillas/activas` - Listar plantillas activas

```- `POST /api/plantillas` - Crear plantilla

- `GET /api/plantillas/{id}` - Obtener plantilla por ID

## 🔧 Comandos Útiles- `PUT /api/plantillas/{id}` - Actualizar plantilla

- `DELETE /api/plantillas/{id}` - Eliminar plantilla

### Desarrollo- `GET /api/plantillas/buscar?nombre={nombre}` - Buscar por nombre

```bash- `PATCH /api/plantillas/{id}/estado` - Cambiar estado

# Ejecutar aplicación- `POST /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Agregar análisis a plantilla

mvn spring-boot:run- `DELETE /api/plantillas/{idPlantilla}/analisis/{idAnalisis}` - Remover análisis de plantilla



# Compilar proyecto### Muestras - CRUD Básico

mvn clean compile- `GET /api/muestras` - Listar muestras con paginación

- `POST /api/muestras` - Crear nueva muestra

# Ejecutar tests- `GET /api/muestras/{id}` - Obtener muestra por ID

mvn test- `GET /api/muestras/numero-interno/{numero}` - Obtener por número interno

- `PUT /api/muestras/{id}` - Actualizar muestra

# Crear JAR- `DELETE /api/muestras/{id}` - Eliminar muestra

mvn clean package

```### Muestras - Búsquedas y Filtros

- `GET /api/muestras/buscar` - Buscar con múltiples criterios

### Base de Datos- `GET /api/muestras/buscar-texto?texto={texto}` - Búsqueda de texto libre

```bash- `GET /api/muestras/cliente/{clienteId}` - Muestras de un cliente específico

# Ver estado de migraciones

mvn flyway:info### Muestras - Gestión de Estados

- `PATCH /api/muestras/{id}/estado?estado={estado}` - Cambiar estado

# Ejecutar migraciones- `POST /api/muestras/{id}/recibir` - Marcar como recibida

mvn flyway:migrate- `POST /api/muestras/{id}/procesar` - Iniciar procesamiento  

- `POST /api/muestras/{id}/completar` - Completar muestra

# Limpiar base de datos (¡CUIDADO!)- `POST /api/muestras/{id}/rechazar` - Rechazar muestra

mvn flyway:clean

```### Muestras - Análisis Individuales

- `POST /api/muestras/{muestraId}/analisis/{analisisId}` - Asignar análisis individual

## 🌟 Funcionalidades Destacadas- `DELETE /api/muestras/{muestraId}/analisis/{analisisId}` - Remover análisis individual

- `GET /api/muestras/{id}/analisis` - Ver análisis asignados a muestra

### Sistema de Estados Granulares- `PATCH /api/muestras/{muestraId}/analisis/{analisisId}/estado` - Cambiar estado de análisis

```

Muestra: REGISTRADA → RECIBIDA → EN_PROCESO → COMPLETADA### Muestras - Plantillas de Análisis  

                                          ↘ RECHAZADA- `POST /api/muestras/{muestraId}/plantillas/{plantillaId}` - Asignar plantilla completa

- `DELETE /api/muestras/{muestraId}/plantillas/{plantillaId}` - Remover plantilla

Análisis: PENDIENTE → EN_PROCESO → COMPLETADO- `GET /api/muestras/{id}/plantillas` - Ver plantillas asignadas a muestra

                               ↘ RECHAZADO- `PATCH /api/muestras/{muestraId}/plantillas/{plantillaId}/estado` - Cambiar estado de plantilla



Plantilla: ASIGNADA → EN_PROGRESO → COMPLETADA### Muestras - Estadísticas y Monitoreo

                                  ↘ CANCELADA- `GET /api/muestras/estadisticas` - Estadísticas completas del sistema

```- `GET /api/muestras/cola-laboratorio` - Cola de trabajo del laboratorio



### Flujos de Trabajo Típicos### Sistema - Monitoreo

- `GET /actuator/health` - Estado de salud del sistema

#### 1. Crear Muestra con Análisis Individual- `GET /actuator/info` - Información del sistema

```bash- `GET /actuator/metrics` - Métricas del sistema

# 1. Crear muestra

POST /api/muestras## Base de Datos

{

  "numeroInterno": "M2024-001",### Esquema Actual (Versión 4)

  "clienteId": "uuid-cliente",La base de datos utiliza PostgreSQL 17.4 con migraciones automáticas vía Flyway.

  "tipoMuestra": "SANGRE",

  "descripcion": "Muestra de rutina"### Tablas Principales

}- **`clientes`** - Información de clientes del laboratorio

- **`analisis`** - Catálogo de análisis con categorías y configuración JSONB

# 2. Asignar análisis individual- **`plantillas`** - Plantillas de procedimientos que agrupan múltiples análisis

POST /api/muestras/{muestraId}/analisis/{analisisId}- **`muestras`** - Muestras de laboratorio con estados y metadatos completos

```

### Tablas de Relación (Many-to-Many)

#### 2. Crear Muestra con Plantilla Completa- **`plantilla_analisis`** - Relación entre plantillas y análisis

```bash- **`muestra_analisis`** - Relación entre muestras y análisis individuales (con estados)

# 1. Crear muestra- **`muestra_plantilla`** - Relación entre muestras y plantillas completas (con estados y orden)

POST /api/muestras

### Características Avanzadas

# 2. Asignar plantilla completa- ✅ **Campos JSONB** - Para configuraciones flexibles en análisis

POST /api/muestras/{muestraId}/plantillas/{plantillaId}- ✅ **Arrays PostgreSQL** - Para almacenar listas de elementos

```- ✅ **Estados granulares** - Control detallado de flujos de trabajo

- ✅ **Triggers y funciones** - Para automación de procesos

#### 3. Gestión de Estados- ✅ **Índices optimizados** - Para búsquedas eficientes

```bash- ✅ **Constraints complejos** - Para integridad de datos

# Cambiar estado de muestra

PATCH /api/muestras/{id}/estado?estado=EN_PROCESO### Migraciones Flyway (Automáticas)

- **V1** - Tabla `clientes` con validaciones básicas

# Cambiar estado de análisis específico- **V2** - Tabla `analisis` con JSONB y arrays

PATCH /api/muestras/{muestraId}/analisis/{analisisId}/estado- **V3** - Tablas `plantillas` y `plantilla_analisis` con relaciones M:N

```- **V4** - Tablas `muestras`, `muestra_analisis` y `muestra_plantilla` con estados avanzados



## 🚀 URLs Importantes## Estructura del Proyecto



- **Aplicación**: http://localhost:8080```

- **Swagger UI**: http://localhost:8080/swagger-ui.htmlsrc/main/java/com/labflow/

- **API Docs JSON**: http://localhost:8080/v3/api-docs├── LabflowBackendApplication.java    # Clase principal Spring Boot

- **Health Check**: http://localhost:8080/actuator/health├── config/

- **Métricas**: http://localhost:8080/actuator/metrics│   └── SecurityConfig.java          # Configuración de seguridad

├── controller/                       # Controladores REST

## 🐛 Solución de Problemas│   ├── AnalisisController.java      # API de análisis

│   ├── ClienteController.java       # API de clientes

### Error: Puerto 8080 en uso│   ├── HealthController.java        # Health checks personalizados

```bash│   ├── MuestraController.java       # API de muestras (completa)

# Cambiar puerto en .env│   └── PlantillaController.java     # API de plantillas

SERVER_PORT=8081├── dto/                             # Data Transfer Objects

```├── exception/                       # Manejo de excepciones

├── model/                           # Entidades JPA

### Error: No se puede conectar a PostgreSQL│   ├── Analisis.java               # Entidad análisis

```bash│   ├── Client.java                 # Entidad cliente

# Verificar servicio de PostgreSQL│   ├── Muestra.java                # Entidad muestra

# Windows: Servicios → PostgreSQL│   ├── MuestraAnalisis.java        # Relación muestra-análisis

# Linux: sudo systemctl status postgresql│   ├── MuestraPlantilla.java       # Relación muestra-plantilla

# macOS: brew services list | grep postgresql│   ├── Plantilla.java              # Entidad plantilla

```│   └── PlantillaAnalisis.java      # Relación plantilla-análisis

├── repository/                      # Repositorios Spring Data

### Error: Base de datos no existe├── service/                         # Lógica de negocio

```bash└── util/                           # Utilidades

# Crear base de datos manualmente

psql -U postgres -c "CREATE DATABASE labflow_db;"src/main/resources/

```├── application.properties           # Configuración principal

└── db/migration/                   # Migraciones Flyway

## 🔐 Notas de Seguridad    ├── V1__Create_clientes_table.sql

    ├── V2__Create_analisis_table.sql

- ⚠️ **Configuración actual**: Optimizada para desarrollo (sin autenticación)    ├── V3__Create_plantillas_table.sql

- 🔒 **Para producción**: Implementar autenticación JWT y roles    └── V4__Create_muestras_table.sql

- 🛡️ **CORS**: Habilitado para desarrollo```

- 🔑 **CSRF**: Deshabilitado para facilitar pruebas

## Configuración de Variables (.env)

## 📈 Próximas Mejoras

```env

- [ ] Sistema de autenticación JWT# Database Configuration

- [ ] Roles y permisos granularesDB_URL=jdbc:postgresql://localhost:5432/labflow_db

- [ ] Auditoría de cambiosDB_USERNAME=labflow_user

- [ ] Notificaciones en tiempo realDB_PASSWORD=labflow_password

- [ ] Export/Import de datos

- [ ] API rate limiting# Server Configuration

- [ ] Cacheing con RedisSERVER_PORT=8080

- [ ] Integración con equipos de laboratorio

# Swagger Configuration

## 🤝 ContribuciónSWAGGER_ENABLED=true



1. Fork el proyecto# Logging Configuration

2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)LOG_LEVEL=INFO

3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)SQL_LOG_LEVEL=DEBUG

4. Push a la rama (`git push origin feature/nueva-funcionalidad`)```

5. Crear Pull Request

## URLs Importantes

## 📄 Licencia

- **Aplicación**: http://localhost:8080

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.- **Swagger UI**: http://localhost:8080/swagger-ui.html

- **API Docs**: http://localhost:8080/v3/api-docs

## 👨‍💻 Desarrollador- **Health Check**: http://localhost:8080/actuator/health

- **Metrics**: http://localhost:8080/actuator/metrics

**LabFlow Team**

- 📧 Email: labflow@example.com## Desarrollo

- 🌐 GitHub: [BartClo/labflow-backend](https://github.com/BartClo/labflow-backend)

### Tecnologías y Versiones

---- **Java**: 21

- **Spring Boot**: 3.5.0

⭐ **¡Dale una estrella al proyecto si te resulta útil!** ⭐- **PostgreSQL**: 17.4
- **Maven**: 3.8+
- **SpringDoc**: 2.6.0

### Características de Desarrollo
- ✅ **Hot Reload** - DevTools habilitado
- ✅ **Live Reload** - Para desarrollo frontend
- ✅ **SQL Logging** - Queries visibles en desarrollo
- ✅ **Validación automática** - Bean Validation habilitado
- ✅ **Swagger integrado** - Documentación en tiempo real
- ✅ **Actuator** - Monitoreo y métricas
- ✅ **Seguridad deshabilitada** - Para desarrollo (sin autenticación)