-- Script para limpiar la migración V8 fallida
-- Ejecutar con: psql -U postgres -d labflow_db -f fix_migration.sql

-- Eliminar tablas si existen
DROP TABLE IF EXISTS config_control_calidad CASCADE;
DROP TABLE IF EXISTS parametro CASCADE;

-- Eliminar funciones y triggers si existen
DROP TRIGGER IF EXISTS trigger_update_config_timestamp ON config_control_calidad;
DROP TRIGGER IF EXISTS trigger_update_parametro_timestamp ON parametro;
DROP FUNCTION IF EXISTS update_config_timestamp();
DROP FUNCTION IF EXISTS update_parametro_timestamp();

-- Eliminar el registro de la migración fallida
DELETE FROM flyway_schema_history WHERE version = '8';

-- Verificar
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
