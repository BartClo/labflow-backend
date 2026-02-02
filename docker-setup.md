# LabFlow Backend - Docker Setup

## 📋 Descripción

Configuración completa de Docker para el backend de LabFlow con las siguientes características de seguridad y rendimiento:

- **Contenedor Java Spring Boot** con usuario no-root y límites de recursos
- **PostgreSQL 15** con health checks y configuración de seguridad
- **Redis 7** para caché y sesiones con contraseña
- **Nginx** como reverse proxy con:
  - Rate limiting (limitación de peticiones)
  - Headers de seguridad
  - Protección anti-smuggling HTTP
  - Protección anti H2C smuggling

## 🚀 Inicio Rápido

### 1. Configurar variables de entorno

```bash
# Copiar el archivo de ejemplo
cp .env.example .env

# Editar .env y configurar tus contraseñas y secretos
# IMPORTANTE: Cambiar TODAS las contraseñas en producción
```

### 2. Levantar los servicios

```bash
# Construir y levantar todos los servicios
docker-compose up -d --build

# Ver logs
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend
```

### 3. Verificar que todo funciona

```bash
# Verificar estado de los contenedores
docker-compose ps

# Probar la API a través de Nginx
curl http://localhost/actuator/health

# Probar la API directamente (sin Nginx)
curl http://localhost:8080/actuator/health
```

## 🏗️ Arquitectura

```
┌─────────────────┐
│   Usuario       │
└────────┬────────┘
         │ HTTP :80
         ▼
┌─────────────────┐
│  Nginx Proxy    │  Rate limiting + Security headers
└────────┬────────┘
         │ :8080
         ▼
┌─────────────────┐
│  Backend Java   │  Spring Boot Application
│  (Spring Boot)  │
└────┬───────┬────┘
     │       │
     │       └──────────┐
     ▼                  ▼
┌──────────┐      ┌──────────┐
│PostgreSQL│      │  Redis   │
│   :5432  │      │  :6379   │
└──────────┘      └──────────┘
```

## 🔒 Características de Seguridad

### Nginx
- **Rate Limiting**:
  - Login: 5 peticiones/minuto
  - General: 30 peticiones/minuto  
  - API: 60 peticiones/minuto
- **Security Headers**:
  - X-Frame-Options: DENY
  - X-Content-Type-Options: nosniff
  - X-XSS-Protection
  - Content-Security-Policy
- **Protección Anti-Smuggling**: Bloquea intentos de H2C smuggling
- **HTTP/2 deshabilitado**: Previene vulnerabilidades conocidas
- **Server tokens ocultos**: No expone versión de Nginx

### Backend
- **Usuario no-root**: Corre como usuario appuser (UID 1001)
- **Capabilities mínimas**: Solo NET_BIND_SERVICE
- **Límites de recursos**: CPU y memoria limitados
- **Health checks**: Verificación automática de salud
- **Alpine Linux**: Imagen mínima para reducir superficie de ataque

### Base de Datos
- **Capabilities restringidas**: Solo las necesarias para PostgreSQL
- **Datos persistentes**: Volumen para /var/lib/postgresql/data
- **Health checks**: Verificación de disponibilidad
- **Red aislada**: Solo accesible desde la red interna

## 📊 Recursos Asignados

| Servicio | CPU (Reserva/Límite) | RAM (Reserva/Límite) |
|----------|---------------------|----------------------|
| Backend  | 0.5 / 2.0 CPUs     | 512M / 2G           |
| Database | 0.5 / 2.0 CPUs     | 512M / 2G           |
| Nginx    | - / 0.5 CPUs       | - / 256M            |
| Redis    | - / -              | - / 256M (maxmemory)|

## 🔧 Comandos Útiles

### Gestión de servicios

```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (¡CUIDADO! Borra la base de datos)
docker-compose down -v

# Reiniciar un servicio específico
docker-compose restart backend

# Ver logs en tiempo real
docker-compose logs -f backend

# Ejecutar comando en un contenedor
docker-compose exec backend sh
docker-compose exec db psql -U postgres -d labflow_db
```

### Desarrollo

```bash
# Reconstruir solo el backend
docker-compose up -d --build backend

# Ver uso de recursos
docker stats

# Limpiar imágenes no usadas
docker image prune -a
```

### Base de datos

```bash
# Backup de la base de datos
docker-compose exec db pg_dump -U postgres labflow_db > backup.sql

# Restaurar backup
docker-compose exec -T db psql -U postgres labflow_db < backup.sql

# Conectarse a PostgreSQL
docker-compose exec db psql -U postgres -d labflow_db
```

## 🌐 Endpoints

- **API (a través de Nginx)**: http://localhost/api/
- **API (directa)**: http://localhost:8080/api/
- **Swagger UI**: http://localhost/swagger-ui.html
- **Health Check**: http://localhost/actuator/health
- **PostgreSQL**: localhost:5432
- **Redis**: localhost:6379

## 🐛 Troubleshooting

### El backend no arranca

```bash
# Ver logs detallados
docker-compose logs backend

# Verificar que la base de datos está lista
docker-compose exec db pg_isready -U postgres

# Reiniciar solo el backend
docker-compose restart backend
```

### Error de conexión a la base de datos

```bash
# Verificar health de PostgreSQL
docker-compose ps

# Ver logs de PostgreSQL
docker-compose logs db

# Verificar variables de entorno
docker-compose config
```

### Problemas de permisos

```bash
# Verificar que los directorios tienen permisos correctos
docker-compose exec backend ls -la /app

# Si hay problemas, reconstruir
docker-compose down
docker-compose up -d --build
```

### Redis no conecta

```bash
# Probar conexión a Redis
docker-compose exec redis redis-cli -a $REDIS_PASSWORD ping

# Ver logs de Redis
docker-compose logs redis
```

## 📝 Notas de Producción

1. **Cambiar TODAS las contraseñas** en `.env`
2. **Configurar CORS** con dominios específicos (no usar `*`)
3. **Usar HTTPS** con certificados SSL (Nginx + Let's Encrypt)
4. **Configurar backups automáticos** de PostgreSQL
5. **Monitorear recursos** con herramientas como Prometheus
6. **Actualizar imágenes** regularmente para parches de seguridad
7. **Desactivar Swagger** en producción o protegerlo con autenticación
8. **Configurar logs centralizados** (ELK, Loki, etc.)

## 🔄 Actualización

```bash
# 1. Hacer backup
docker-compose exec db pg_dump -U postgres labflow_db > backup_$(date +%Y%m%d).sql

# 2. Detener servicios
docker-compose down

# 3. Actualizar código
git pull

# 4. Reconstruir y levantar
docker-compose up -d --build

# 5. Verificar logs
docker-compose logs -f
```

## 📚 Referencias

- [Docker Compose documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Nginx security best practices](https://nginx.org/en/docs/)
- [PostgreSQL Docker image](https://hub.docker.com/_/postgres)
