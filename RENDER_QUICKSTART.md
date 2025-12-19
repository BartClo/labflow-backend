# ⚡ GUÍA RÁPIDA - Despliegue en Render

## ✅ Archivos Creados (Ya listos en tu proyecto)

- `render.yaml` - Configuración automática
- `build.sh` - Script de construcción  
- `system.properties` - Versión de Java 21
- `application-prod.properties` - Config de producción
- `.dockerignore` - Optimización de build

## 🚀 PASOS RÁPIDOS

### 1. Sube tu código a GitHub (si no lo has hecho)
```bash
git init
git add .
git commit -m "Deploy to Render"
git remote add origin https://github.com/TU_USUARIO/labflow-backend.git
git push -u origin main
```

### 2. Crea PostgreSQL en Render
- Ve a https://dashboard.render.com
- New + → PostgreSQL
- Name: `labflow-db`
- Plan: Free
- Create Database
- **GUARDA la "Internal Database URL"**

### 3. Crea Web Service
- New + → Blueprint (usa render.yaml)
- O New + → Web Service (configuración manual)
- Conecta tu repo de GitHub
- Render detectará el `render.yaml` automáticamente

### 4. Variables de Entorno (Importante)

En el Web Service, añade estas variables:

```
SPRING_PROFILES_ACTIVE=prod
DB_URL=[Pega aquí la Internal Database URL]
DB_USERNAME=labflow_user
DB_PASSWORD=[Pega la contraseña de PostgreSQL]
SERVER_PORT=8080
SWAGGER_ENABLED=true
LOG_LEVEL=INFO
JAVA_OPTS=-Xmx512m -Xms256m
```

### 5. Deploy
- Click "Create Web Service"
- Espera 5-10 minutos
- Tu app estará en: `https://labflow-backend.onrender.com`

## 🔍 Verificar que funciona

1. **Health**: https://labflow-backend.onrender.com/actuator/health
2. **Swagger**: https://labflow-backend.onrender.com/swagger-ui.html

## ⚠️ Importante

- Plan Free: se suspende tras 15 min de inactividad
- Primera petición después = 30-60 seg (cold start)
- Base de datos expira en 90 días (plan free)

## 📖 Documentación Completa

Ver [DEPLOY_RENDER.md](DEPLOY_RENDER.md) para guía detallada con troubleshooting.

---
**¿Problemas?** Revisa los logs en Render Dashboard → Web Service → Logs
