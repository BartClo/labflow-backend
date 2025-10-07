-- ======================================================
-- Script de configuración de base de datos LabFlow
-- ======================================================
-- Este script crea la base de datos y usuario para el proyecto LabFlow Backend
-- Ejecutar como superusuario de PostgreSQL (postgres)

-- 1. Crear la base de datos principal
CREATE DATABASE labflow_db
    WITH 
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TEMPLATE = template0;

-- 2. Crear usuario específico para LabFlow
CREATE USER labflow_user WITH PASSWORD 'labflow_password_2024';

-- 3. Otorgar privilegios al usuario sobre la base de datos
GRANT ALL PRIVILEGES ON DATABASE labflow_db TO labflow_user;

-- 4. Conectar a la base de datos labflow_db
\c labflow_db;

-- 5. Otorgar privilegios sobre el esquema public
GRANT ALL ON SCHEMA public TO labflow_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO labflow_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO labflow_user;

-- 6. Configurar privilegios por defecto para futuras tablas
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO labflow_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO labflow_user;

-- 7. Verificar la creación
SELECT datname FROM pg_database WHERE datname = 'labflow_db';
SELECT usename FROM pg_user WHERE usename = 'labflow_user';

-- ======================================================
-- Comandos para ejecutar en orden:
-- ======================================================
-- 1. Conectar a PostgreSQL como superusuario:
--    psql -U postgres -h localhost
-- 
-- 2. Ejecutar este script:
--    \i setup_labflow_db.sql
-- 
-- 3. Verificar conexión con el nuevo usuario:
--    psql -U labflow_user -d labflow_db -h localhost
-- ======================================================