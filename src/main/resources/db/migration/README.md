# Migraciones de Base de Datos LabFlow

Este directorio contiene las migraciones de base de datos utilizando Flyway.

## Convención de Nomenclatura

Los archivos de migración siguen el patrón: `V{version}__{description}.sql`

Ejemplo: `V1__Create_clientes_table.sql`

## Migraciones Existentes

### V1__Create_clientes_table.sql
- **Descripción**: Crea la tabla de clientes con UUID como primary key
- **Contenido**:
  - Tabla `clientes` con campos:
    - `id_cliente` (UUID, Primary Key)
    - `nombre_cliente` (VARCHAR(255), NOT NULL)
    - `fecha_creacion` (TIMESTAMP)
    - `fecha_actualizacion` (TIMESTAMP)
  - Índice para búsquedas por nombre
  - Trigger automático para actualizar `fecha_actualizacion`
  - Extensión UUID habilitada

## Cómo Funcionan las Migraciones

1. **Automático**: Al iniciar la aplicación, Flyway ejecuta automáticamente las migraciones pendientes
2. **Orden**: Las migraciones se ejecutan en orden de versión (V1, V2, V3, etc.)
3. **Una sola vez**: Cada migración se ejecuta solo una vez
4. **Validación**: Flyway valida que las migraciones no hayan cambiado

## Comandos Útiles

### Información de Migraciones
```bash
# Ver estado de migraciones
mvn flyway:info

# Validar migraciones
mvn flyway:validate

# Reparar historial de migraciones (solo si es necesario)
mvn flyway:repair
```

### Aplicar Migraciones Manualmente
```bash
# Ejecutar migraciones pendientes
mvn flyway:migrate
```

## Crear Nueva Migración

1. **Crear archivo** en este directorio con el formato: `V{next_version}__{description}.sql`
2. **Escribir SQL** para los cambios necesarios
3. **Reiniciar aplicación** o ejecutar `mvn flyway:migrate`

## Notas Importantes

- ⚠️ **Nunca modifiques** una migración que ya se ha ejecutado
- ✅ **Siempre crea** una nueva migración para cambios adicionales
- 🧪 **Prueba migraciones** en entorno de desarrollo antes de producción
- 📝 **Documenta** los cambios en comentarios SQL

## Troubleshooting

### Error: "Validate failed: Migration checksum mismatch"
**Solución**: No modificar migraciones ya ejecutadas. Crear nueva migración.

### Error: "Found non-empty schema without schema history table"
**Solución**: Configurado `baseline-on-migrate=true` en application.properties

### Verificar Estado de la Base de Datos
```sql
-- Ver tabla de historial de Flyway
SELECT * FROM flyway_schema_history ORDER BY installed_rank;

-- Verificar tabla clientes
SELECT * FROM clientes LIMIT 5;
```