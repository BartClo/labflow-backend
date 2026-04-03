# 📘 Documentación Backend - LabFlow

**Versión:** 2.0 (Funcional)  
**Actualizado:** Abril 2026  
**Propósito:** Sistema de gestión integral de laboratorio clínico  
**Público:** Administradores, Técnicos, Receptores, Clientes

---

## 📋 Tabla de Contenidos

1. [¿Qué es LabFlow?](#qué-es-labflow)
2. [¿Qué puedes hacer con LabFlow?](#qué-puedes-hacer-con-labflow)
3. [Usuarios y sus Funciones](#usuarios-y-sus-funciones)
4. [Flujos de Trabajo en el Laboratorio](#flujos-de-trabajo-en-el-laboratorio)
5. [Gestión de Datos](#gestión-de-datos)
6. [Seguridad y Privacidad](#seguridad-y-privacidad)
7. [Monitoreo del Sistema](#monitoreo-del-sistema)
8. [Cómo Ejecutar el Sistema](#cómo-ejecutar-el-sistema)

---

## ¿Qué es LabFlow?

**LabFlow Backend** es el "corazón inteligente" de un **sistema de laboratorio clínico digital**. 

Imagina que diriges un laboratorio donde llegan muestras de sangre, orina y otros fluidos cada día. Antes de LabFlow, todo era manual en papel:
- Escribir qué muestra llega
- Anotar qué análisis hacer
- Escribir los resultados
- Imprimir reportes manuales
- Perder tiempo buscando información

**LabFlow automatiza TODO esto.** El backend es quien "piensa" y procesa toda la información del laboratorio.

### Lo que LabFlow Hace Automáticamente

1. **Recibe muestras** → Registra cada muestra que llega (sangre, orina, etc.) con código de barras
2. **Organiza el trabajo** → Crea órdenes de trabajo para decirle a los técnicos qué análisis hacer
3. **Controla el progreso** → Sigue cada muestra desde que llega hasta que se entrega el resultado
4. **Almacena resultados** → Guarda todos los resultados de análisis de forma segura
5. **Valida la calidad** → Verifica que los resultados estén dentro de los rangos normales
6. **Genera reportes** → Crea automáticamente reportes en PDF profesionales
7. **Controla usuarios** → Cada persona solo ve lo que puede hacer según su rol
8. **Protege información** → Usa contraseñas seguras y cifrado para proteger datos de pacientes
9. **Supervisa el sistema** → Monitorea si el laboratorio está funcionando correctamente

### Beneficios Principales

| Beneficio | Antes de LabFlow | Con LabFlow |
|-----------|-----------------|-----------|
| **Búsqueda de muestra** | 10 minutos hojeando archivos | 10 segundos en el sistema |
| **Errores en datos** | Frecuentes (escritura manual) | Minimizados (validaciones automáticas) |
| **Reportes** | Generados manualmente en 1 hora | Generados en 1 minuto automático |
| **Auditoría** | Sin registro de quién hizo qué | Registro completo de toda actividad |
| **Acceso de clientes** | Llamar al laboratorio | Ver resultados en portal web 24/7 |
| **Informes mensuales** | Contados manualmente | Generados automáticamente |

---

## ¿Qué puedes hacer con LabFlow?

### Para la Recepción (Área de Entrada)

**La muestra llega al laboratorio:**

1. **Registrar la muestra**
   - Receptor escanea el código de barras
   - Sistema verifica automáticamente que no existe duplicado
   - Registra: tipo (sangre/orina), cliente, prioridad
   - La muestra entra en el sistema

2. **Verificar integridad**
   - Sistema valida que la muestra cumple requisitos
   - Notifica si hay problema (refrigeración, contaminación, etc.)
   - Solo muestras válidas avanzan

**Ejemplo:** El lunes llegan 50 muestras. El receptor escanea cada una y LabFlow las registra automáticamente en 15 minutos, en lugar de llenar 50 papeles.

### Para los Técnicos (Laboratorio)

**El técnico recibe su lista de trabajo:**

1. **Ver órdenes de trabajo** 
   - Ve qué análisis tiene que hacer en cada muestra
   - Ve en qué etapa de proceso está cada orden
   - Prioridades destacadas en rojo para atender primero

2. **Realizar análisis**
   - Sigue los pasos del protocolo de análisis
   - Registra valores del equipo (glucosa: 120 mg/dL, etc.)
   - Marca análisis como completo

3. **Validar resultados**
   - Revisa si los valores están dentro del rango normal
   - Si están "raros", el sistema lo marca para revisar
   - Aprueba o rechaza resultados

4. **Marcar etapas completadas**
   - Al terminar recepción de muestra → marca "recepción completa"
   - Al terminar análisis → marca "análisis completo"
   - Al validar → marca "validación completa"
   - Sistema avanza automáticamente

**Ejemplo:** Técnico Juan entra y ve: "Tienes 8 órdenes, 3 urgentes (rojo)". Hace los análisis, registra valores, valida. Cuando termina, LabFlow genera el reporte automático.

### Para los Administradores

**Control total del laboratorio:**

1. **Crear usuarios**
   - Crear cuentas para técnicos, receptores, otros
   - Asignar roles (¿puede ver todo? ¿solo su trabajo?)
   - Cambiar contraseñas de personas que las olvidaron

2. **Gestionar análisis**
   - Crear nuevo tipo de análisis (glucosa, hemoglobina, etc.)
   - Definir rangos normales (hombre: 15-17, mujer: 13-16 para hemoglobina)
   - Definir límites de alerta (qué valor es "peligroso")

3. **Ver reportes de gestión**
   - Muestras procesadas hoy: 150
   - Muestras rechazadas: 3
   - Tiempo promedio por análisis: 45 minutos
   - Técnico más productivo: Juan (50 análisis)

4. **Control de calidad**
   - Ver tendencias de resultados
   - Detectar si hay problema con equipos
   - Auditoría: ver quién hizo qué y cuándo

**Ejemplo:** Admi María revisa: "Los análisis de glucosa del equipo C tienen valores anómalos últimamente". Anota para recalibrar el equipo.

### Para los Clientes

**Ver sus resultados:**

1. **Acceder a portal** 
   - Entra con sus credenciales (seguro, solo VE SUS datos)
   - Ve todas sus muestras procesadas

2. **Descargar reportes**
   - Reportes en PDF profesional
   - Incluye valores, interpretación, firma del análisis
   - Puede descargar cuando necesite

**Ejemplo:** El cliente "Hospital Central" entra y descarga: "Resultados análisis muestra M-2456 Paciente Juan Pérez". Tiene valores, interpretación médica, sello del laboratorio.

---

## Usuarios y sus Funciones

### Cuatro Roles Principales

| Rol | ¿Quién es? | ¿Qué puede HACER? | ¿Qué NO puede hacer? |
|-----|-----------|------------------|-------------------|
| **ADMINISTRADOR** | Gerente del laboratorio | Crear/editar usuarios, definir análisis, ver reportes gerenciales, auditoría completa | No puede borrar datos históricos |
| **TÉCNICO** | Persona que hace análisis | Ver órdenes asignadas, registrar resultados, validar análisis, completar etapas | No puede crear usuarios ni gestionar sistema |
| **RECEPTOR** | Recibe muestras | Registrar muestras, crear órdenes de trabajo iniciales | No puede ver resultados ni validar análisis |
| **CLIENTE** | Hospital/clínica que envía muestras | Ver sus muestras, descargar reportes | No puede ver datos de otros clientes |

### Flujo de Acceso

```
Usuario ingresa email y contraseña
    ↓
LabFlow verifica que email existe
    ↓
LabFlow verifica que contraseña es correcta
    ↓
LabFlow carga el rol del usuario
    ↓
Usuario entra a su "dashboard" personalizado
    ↓
    Si es ADMIN:  ve panel de gestión + usuarios + reportes
    Si es TÉCNICO:  ve sus órdenes de trabajo
    Si es RECEPTOR:  ve formulario de registro de muestras
    Si es CLIENTE:  ve solo sus muestras y reportes
```

### ¿Cómo se Protege la Privacidad?

Un paciente diabético no quiere que su resultado de glucosa lo vea el técnico de otra clínica. LabFlow lo previene:

- Cada usuario solo ve información de su rol
- Si cliente "Hospital A" entra, NO VE datos de "Hospital B"
- Si técnico entra, NO PUEDE VER datos de clientes
- Administrador ve TODO (pero login queda registrado en auditoría)
- Logs guardan: quién entró, a qué hora, qué hizo

---

## Flujos de Trabajo en el Laboratorio

### El Viaje de una Muestra: Paso a Paso

**Muestra: "M-001 Sangre de Juan Pérez" entra el lunes a las 09:00**

#### Etapa 1: RECEPCIÓN (09:00 - 09:15)

```
Receptor escanea código de barras "BC123456"
    ↓
LabFlow registra:
  • Muestra: M-001
  • Código: BC123456
  • Tipo: Sangre
  • Cliente: Centro Médico XYZ
  • Análisis: Hemograma
    ↓
Estado: ✓ RECIBIDA CORRECTAMENTE
    ↓
Muestra lista para análisis
```

**¿Qué valida LabFlow aquí?**
- Código no existe antes (no duplicada)
- Código tiene formato correcto
- Cliente existe en sistema
- Tipo de análisis existe

---

#### Etapa 2: ANÁLISIS (09:15 - 10:30)

```
Técnico María ve en su pantalla:
  ⚠️  URGENTE (rojo)
  Orden: OT-2026-001
  Muestra: M-001
  Análisis: Hemograma
  Prioridad: Máxima

María click: "Iniciar análisis"

LabFlow registra:
  • Hora inicio: 09:15
  • Estado: EN PROGRESO
  • Técnico: María García

María realiza análisis y registra valores:
  • Hemoglobina: 14.2 g/dL
  • Hematocrito: 42.5%
  • Leucocitos: 7,200/μL

Sistema valida automáticamente:
  ✓ 14.2 g/dL está en rango normal
  ✓ 42.5% está en rango normal
  ✓ 7,200 está en rango normal

Status: TODO NORMAL ✓
```

---

#### Etapa 3: VALIDACIÓN (10:30 - 10:45)

```
Supervisor Pedro revisa resultados:
  Hemograma M-001
  Técnico: María García
  Resultados: Todos en rango ✓
  
Verifica:
  ✓ Valores coherentes?
  ✓ Técnica correcta?
  ✓ No hay interferencias?
  
Pedro click: "APROBAR"

LabFlow registra:
  • Validado por: Pedro López
  • Hora: 10:45
  • Status: VALIDADO
```

---

#### Etapa 4: REPORTE (10:45 - 10:50)

```
LabFlow genera automáticamente PDF profesional:

LABORATORIO CLÍNICO XYZ
RESULTADO DE ANÁLISIS

PACIENTE: Juan Pérez García
MUESTRA: M-001 (Sangre)
FECHA ANÁLISIS: 03/04/2026

HEMOGRAMA

Parámetro              | Resultado | Rango Normal | Estado
Hemoglobina            | 14.2 g/dL | 13-16       | NORMAL ✓
Hematocrito            | 42.5%     | 38-46%      | NORMAL ✓
Leucocitos             | 7200/μL   | 4500-11000  | NORMAL ✓

INTERPRETACIÓN: Valores completamente normales.
No signos de anemia ni alteraciones.

Validado por: Pedro López, BQ Técnico
Disponible en portal para cliente: 03/04/2026 10:50
```

**Tiempo total: 1 hora 50 minutos**

Si lo hubiera hecho manualmente: 3-4 horas

---

### Casos Especiales

#### ¿Qué pasa si hay un valor ANÓMALO?

```
María ingresa: Hemoglobina: 7.2 g/dL ← CRÍTICO (rango: 13-16)

LabFlow ALERTA AUTOMÁTICAMENTE:
  ⚠️  ALERTA CLÍNICA
  
  Hemoglobina está MUY BAJA
  Valor: 7.2 (rango 13-16)
  Severidad: CRÍTICA
  
  Acciones:
  □ Repetir muestra
  □ Revisar protocolo
  □ Aceptar valor y documentar

Pedro revisa: "Posible anemia grave. Repetir análisis."
LabFlow activa nueva orden automáticamente.
```

#### ¿Qué pasa si un CLIENTE necesita su resultado?

**Martes 14:00 - Cliente entra a portal**

```
Mi Portal - Centro Médico XYZ

Mis muestras en proceso:
  • M-001: Hemograma    [✓ LISTO]
  • M-002: Química      [EN PROGRESO]
  • M-003: Serología    [RECIBIDA]

Click en M-001:
[DESCARGAR PDF REPORTE]

Sistema registra:
  • Cliente vio resultado: 03/04/2026 14:15
  • Cliente descargó: 03/04/2026 14:16
  • Auditoría: Acceso registrado ✓
```

---

## Gestión de Datos

### La Información que LabFlow Almacena

**Para cada MUESTRA:**
- ID único (M-001, M-002, etc.)
- Código de barras (para escaneo rápido)
- Tipo (sangre, orina, etc.)
- Cliente que la envió
- Fecha/hora de recepción
- Prioridad (baja/media/alta/urgente)
- Estado actual (recibida/análisis/validada/completa)

**Para cada ANÁLISIS:** 
- Nombre (Hemograma, Glucosa, etc.)
- Valores medidos (14.2, 120, etc.)
- Rangos normales (mín-máx)
- Valores críticos (cuándo alertar)
- Técnico que lo hizo
- Validador que lo aprobó
- Fecha/hora exacta
- Resultado PDF

**Para cada USUARIO:**
- Nombre completo
- Email (único)
- Contraseña (hash seguro, no se guarda en texto plano)
- Rol (admin/técnico/receptor/cliente)
- Fecha de creación
- Última vez que entró

**Para cada ORDEN DE TRABAJO:**
- Código único (OT-2026-001)
- Cliente y muestras asociadas
- Análisis solicitados
- Técnico asignado
- 4 Etapas: recepción, análisis, validación, reporte
- Fecha de creación y finalización
- Estado actual

### Cómo LabFlow Previene Pérdida de Datos

1. **Respaldos automáticos diarios**
   - Cada noche a las 01:00 AM hace copia de toda la BD
   - Copias guardadas en servidor seguro
   - Si algo falla, se recupera del respaldo

2. **Registro de cambios (Auditoría)**
   - Cada cambio queda registrado
   - Quién hizo cambio, cuándo, QUÉ cambió
   - No se puede borrar datos, solo marcar como "inactivo"

3. **Control de versiones**
   - Todas las migraciones son numeradas (V1, V2, V3...)
   - Si versión falla, se revierte a la anterior
   - Base de datos siempre está en estado consistente

---

## Seguridad y Privacidad

### Autenticación: ¿Cómo Sabes que eres TÚ?

**Cuando ingresas tu email y contraseña:**

```
1. Escribes en pantalla:
   Email: maria@labflow.com
   Contraseña: ••••••••••

2. Navegador envía información encriptada al servidor

3. LabFlow verifica:
   ✓ ¿Este email existe?
   ✓ ¿Esta contraseña es correcta?
   ✓ ¿Esta cuenta está activa?
   ✓ ¿No hay intentos sospechosos?

4. LabFlow genera "llave temporal de 24 horas":
   Esta llave es como un pase que dice:
   "Soy María, me logueé el 03/04/2026 a las 09:00"

5. Cada vez que pides datos:
   Frontend envía: "Quiero ver mis órdenes [aquí va mi llave]"
   Backend verifica: "Sí, esta llave es válida, eres María"
   Backend te muestra tus datos

6. Después de 24 horas:
   Tu llave expira. Necesitas volver a loguear.
```

### Protección de Contraseñas

**Jamás guardamos tu contraseña en texto plano.**

La contraseña se transforma en una "huella digital" que:
- No se puede revertir a la contraseña original
- Si alguien roba la huella, no sirve
- Cada vez que ingresas, se convierte a huella y compara
- Si huelas coinciden, eres tú ✓

**Algoritmo usado: Argon2**
- Ganador de competencia internacional de seguridad
- Resistente a ataques sofisticados
- Mucho más seguro que métodos antiguos

### Roles y Permisos: ¿Quién puede VER qué?

**ADMINISTRADOR:**
- ✅ Ve todas las muestras del laboratorio
- ✅ Ve todos los análisis
- ✅ Ve quién entró y qué hizo (auditoría)
- ❌ NO puede ver datos de otros laboratorios

**TÉCNICO:**
- ✅ Ve sus órdenes asignadas
- ✅ Ve resultados que debe procesar
- ❌ NO ve datos administrativos
- ❌ NO ve datos de otros técnicos

**RECEPTOR:**
- ✅ Ve formulario para registrar muestras
- ❌ NO ve resultados
- ❌ NO ve datos técnicos

**CLIENTE:**
- ✅ Ve solo sus muestras
- ✅ Ve solo sus resultados
- ✅ VE su información de facturación
- ❌ NO ve datos de otros clientes

### Comunicación Segura (HTTPS)

Cuando navegador ↔ servidor comunican:

```
Frontend (tu computadora)      Backend (servidor)
    ↓                              ↓
Encripta con certificado    Verifica certificado
    ↓                              ↓
Envía datos encriptados    Solo lee datos encriptados
    ↓────────HTTPS Tunnel────────→ ↓

(Imposible que alguien en WiFi vea tus datos en tránsito)
```

En producción: HTTPS obligatorio  
En desarrollo: HTTP seguro (solo test)

### Validación de Datos: Prevenir Ataques

Alguien malicioso intenta:
```
"Quiero ver datos del cliente #1"
```

**SIN PROTECCIÓN:**
→ Backend: "OK, aquí están datos del cliente 1"
→ Hacker ve datos de OTRO cliente!

**CON PROTECCIÓN LabFlow:**
1. Frontend verifica: "¿Estoy logueado como CLIENTE?" Si no, rechazo.
2. Si soy cliente, verifica: "¿Cliente 1 es MI cliente?" Si no, rechazo.
3. Backend verifica también: "¿Este usuario tiene permiso?" Si no, rechazo.

**Triple validación = imposible ver datos no autorizados**

### Auditoría Completa

LabFlow registra TODO:

```
Evento: Usuario "maria@lab.com" entró
Timestamp: 03/04/2026 09:00:15
IP: 192.168.1.100
Status: ✓ Éxito

Evento: Usuario "maria@lab.com" modificó muestra M-001
Timestamp: 03/04/2026 09:20:10
Cambios: ["estado: RECIBIDA → ANÁLISIS"]

Evento: Usuario "maria@lab.com" cerró sesión
Timestamp: 03/04/2026 17:00:00
```

**¿Para qué sirve?**
- Si hay problema, investigar QUÉ pasó
- Cumplimiento regulatorio (leyes de privacidad)
- Detectar intentos de acceso no autorizados
- Responsabilidad legal

---

## Monitoreo del Sistema

### ¿Cómo Sé que LabFlow Está Funcionando?

**Cada segundo, LabFlow se "pregunta a sí mismo":**

```
✓ ¿Base de datos conectada?        → SÍ
✓ ¿Servidor respondiendo?           → SÍ  (~50ms)
✓ ¿Disco tiene espacio?             → SÍ  (850 GB libres)
✓ ¿Memoria RAM suficiente?          → SÍ  (6.2 GB / 8 GB)
✓ ¿CPU con uso razonable?           → SÍ  (25%)
✓ ¿Sin errores en últimas horas?   → SÍ

Status GENERAL: 🟢 VERDE - Todo Bien
```

### Métricas que se Monitorean

| Métrica | ¿Qué significa? | ¿Qué es "bueno"? | ¿Qué es "malo"? |
|---------|-----------------|------------------|-----------------|
| **Tiempo de respuesta** | Cuánto tarda en responder | < 500ms | > 2000ms |
| **Requests por segundo** | Cuántas personas usan al tiempo | 100/seg | 500/seg (sobrecarga) |
| **Memoria usada** | Cuánta RAM usa | 60% | 95%+ (va a fallar) |
| **Errores por hora** | Cuántos fallos hay | 0 | 10+ (problema) |
| **BD latencia** | Cuánto tarda en guardar | < 100ms | > 500ms (lento) |
| **Disponibilidad** | Porcentaje de tiempo "arriba" | 99.9% | 95% (inaceptable) |

### Panel de Control (Grafana)

**Los Administradores ven un dashboard:**

```
╔═══════════════════════════════════════╗
║        SALUD DEL SISTEMA              ║
╠═══════════════════════════════════════╣
║                                       ║
║  Uptime:  ██████████░░░░░  45 días  ║
║  CPU:     ███░░░░░░░░░░░░░  35%     ║
║  RAM:     ██████░░░░░░░░░░  62%     ║
║  Disco:   ██████████░░░░░░  75%     ║
║                                       ║
║  Requests última hora:       3,450    ║
║  Errores última hora:           2     ║
║  Usuarios conectados:          12     ║
║                                       ║
║  Endpoint más usado: /api/muestras   ║
║  Endpoint más lento: /api/informes   ║
║                                       ║
╚═══════════════════════════════════════╝
```

### Alertas Automáticas

Si algo sale mal, LabFlow NOTIFICA:

```
ALERTA: Uso de memoria en 92%
Recomendación: "Reinicia servidor o aumenta RAM"

ALERTA: Base de datos sin responder
Recomendación: "Verifica conexión BD"

ALERTA: Más de 50 errores en 1 hora
Recomendación: "Busca error en logs"
```

---

## Cómo Ejecutar el Sistema

### Lo Mínimo que Necesitas

1. **Java** (lenguaje que usa LabFlow)
2. **PostgreSQL** (donde se guardan los datos)
3. **Conexión a internet** (para descargar dependencias)
4. **Terminal/Línea de comandos**

**Verificar que tienes todo:**

```bash
# Verificar Java
java -version
# Debería salir: openjdk version "21" o similar

# Verificar PostgreSQL  
psql --version
# Debería salir: PostgreSQL versión 14 o similar
```

### Instalación en Tu Máquina

```bash
# 1. Descarga el código
git clone https://github.com/tu-repo/labflow-backend.git
cd labflow-backend

# 2. Crea la base de datos
psql -U postgres -c "CREATE DATABASE labflow_db;"

# 3. Compila el código
mvn clean install

# 4. Ejecuta el sistema
mvn spring-boot:run

# 5. Abre navegador en:
# http://localhost:8080
```

Esperas ver:
```
Started LabFlow in 12.345 seconds
✓ Servidor corriendo
```

### Con Docker (Fácil)

Si tienes Docker instalado:

```bash
# Descarga código
git clone https://github.com/tu-repo/labflow-backend.git
cd labflow-backend

# Ejecuta todo en un comando
docker-compose up -d

# Espera 10 segundos y prueba:
curl http://localhost:8080/health
```

Salida esperada:
```json
{
  "status": "UP",
  "database": "connected"
}
```

### Primeros Pasos Después de Instalar

**1. Crear cuenta de Administrador:**

```bash
# URL especial para primer admin:
http://localhost:8080/api/usuarios/setup

# Formulario:
Nombre: Carlos
Email: carlos@labflow.com
Contraseña: TempPassword123!
Rol: ADMIN
```

**2. Entrar al sistema:**

```
Email: carlos@labflow.com
Contraseña: TempPassword123!
```

**3. Cambiar contraseña:**
- Click "Mi Perfil"
- Click "Cambiar Contraseña"
- Ingresa contraseña más segura

**4. Crear primer técnico:**
- Panel Admin → Usuarios
- Click "+ Nuevo Usuario"
- Nombre: "Juan García"
- Email: "juan@labflow.com"
- Rol: "TÉCNICO"
- Click "Crear"

---

**Última actualización:** Abril 2026  
**Documentación versión:** 2.0 (Funcional, no técnica)  
**Contacto:** documentacion@labflow.com