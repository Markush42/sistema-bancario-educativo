## Fase 1 — Integración técnica del MVP con Spring Boot 3.3.x (LTS)

Esta fase implementa el MVP descrito en `01-vision-alcance.md` utilizando Spring Boot
3.3.x (LTS) y las dependencias recomendadas. El objetivo es tener una base ejecutable
que cubra gestión de empleados, clientes, cuentas, operaciones básicas y auditoría.

### 1. Dependencias clave y qué requisito habilitan

| Requisito MVP | Dependencia / Módulo | Rol técnico |
| --- | --- | --- |
| Autenticación mínima de empleados y roles ADMIN/OPERADOR | `spring-boot-starter-security`, `spring-security-crypto` | Seguridad HTTP básica (login form o basic auth) y hashing de credenciales. |
| Gestión de empleados y clientes vía API | `spring-boot-starter-web`, `spring-boot-starter-validation`, `mapstruct` | Controladores REST, validación de DTOs y mapeo de entidades a respuestas. |
| Persistencia de clientes, cuentas y movimientos | `spring-boot-starter-data-jpa`, driver `postgresql` o `mysql-connector-j` | Repositorios JPA para CRUD y consultas de saldos. |
| Migraciones de esquema inicial (empleados, clientes, cuentas, movimientos, auditoría) | `flyway-core` (o `liquibase-core`) | Versionar la base y reproducir ambientes. |
| Registro de operaciones (auditoría básica) | `spring-boot-starter-data-jpa`, `spring-boot-starter-security` | Persistir qué empleado ejecutó la operación y cuándo. |
| Exposición y exploración de APIs | `springdoc-openapi-starter-webmvc-ui` | Swagger UI para probar endpoints en desarrollo. |
| Observabilidad y health checks | `spring-boot-starter-actuator` | Health, métricas y readiness/liveness simples. |
| Pruebas de integración realistas | `spring-boot-starter-test`, `testcontainers` (PostgreSQL) | Subir BD efímera y probar reglas de negocio (saldos, transferencias). |

### 2. Alcance funcional cubierto en esta fase

1. **Empleados y roles**: endpoints para alta y login básico (HTTP Basic o formulario) con
   control de acceso a operaciones críticas.
2. **Clientes**: CRUD con validación de datos obligatorios y estados (activo/bloqueado).
3. **Cuentas**: apertura de `CAJA_AHORRO` y `CUENTA_CORRIENTE`, consulta de estado y saldo.
4. **Operaciones**: depósito, extracción con control de saldo, transferencia interna con dos
   movimientos atómicos.
5. **Auditoría mínima**: persistencia del usuario autenticado y timestamp en cada movimiento.
6. **Observabilidad**: endpoints de health/metrics para validar despliegues dev/test.

### 3. Entregables de la fase

- Proyecto Spring Boot 3.3.x con BOM `spring-boot-dependencies` gestionando versiones.
- Scripts Flyway para tablas: empleados, roles, clientes, cuentas, movimientos, auditoría.
- Controladores REST y DTOs validados para cada caso de uso del MVP.
- Seguridad básica configurada (en memoria o BD) con roles ADMIN/OPERADOR.
- Colección de pruebas (JUnit + Testcontainers) cubriendo reglas críticas de negocio.
- Documentación OpenAPI expuesta en `/swagger-ui.html` para verificación manual.

### 4. Secuencia sugerida (sprint único de arranque)

1. **Infraestructura**: crear proyecto, aplicar BOM 3.3.x, agregar dependencias base,
   configurar Actuator.
2. **Datos**: modelar entidades y migraciones Flyway; levantar BD local/Testcontainers.
3. **Seguridad**: configurar Spring Security mínima y roles; proteger endpoints sensibles.
4. **Casos de uso**: implementar controladores/servicios para empleados, clientes, cuentas
   y operaciones con validaciones de negocio.
5. **Auditoría**: capturar usuario autenticado en movimientos y exponer consultas.
6. **Pruebas**: integrar Testcontainers y escribir casos de integración para depósitos,
   extracciones y transferencias.
7. **Docs DX**: habilitar Swagger UI y añadir notas de uso en README/portal interno.

### 5. Criterios de aceptación técnicos

- Build exitoso con las dependencias definidas en el `pom.xml` (o `build.gradle`).
- Migraciones iniciales ejecutan en local y en entorno de CI mediante Testcontainers.
- Todos los endpoints del MVP autenticados y protegidos por rol cuando aplica.
- Operaciones financieras rechazan saldos insuficientes y mantienen consistencia de saldos.
- Health check (`/actuator/health`) responde `UP` en despliegue de referencia.
