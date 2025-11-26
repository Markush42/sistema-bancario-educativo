# 11 — Módulos y Responsabilidades del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define los **módulos funcionales** del sistema bancario educativo y las
**responsabilidades principales** de cada uno, alineados con:

- La **visión del sistema** (`01-vision-alcance.md`).
- Los **requisitos funcionales** (`04-requisitos-funcionales.md`).
- Las **historias de usuario** (`06-user-stories.md`).
- El **modelo de entidades** (`07-entidades-dominio.md`, `08-entidades-detalle.md`).
- El **modelo conceptual** (`09-modelo-conceptual.md`).
- La **arquitectura en capas** (`10-arquitectura-capas.md`).

Su objetivo es servir de puente entre:

- Lo que el sistema **debe hacer** (negocio).
- Y **dónde** se implementa cada responsabilidad (módulo + capa).

---

## 2. Lista de módulos del MVP

El MVP se organiza en los siguientes **módulos funcionales**:

| Código | Módulo                      | Descripción breve                                                      |
|--------|-----------------------------|-------------------------------------------------------------------------|
| MOD-IA | Identidad & Acceso          | Autenticación de empleados, gestión básica de sesión y roles.          |
| MOD-EMP| Gestión de Empleados        | Alta, modificación, bloqueo y asignación de roles de empleados.        |
| MOD-CLI| Gestión de Clientes         | Alta, actualización y bloqueo de clientes bancarios.                   |
| MOD-CTA| Gestión de Cuentas          | Apertura de cuentas y consultas de saldos y movimientos.               |
| MOD-OP | Operaciones Financieras     | Depósitos, extracciones y transferencias internas entre cuentas.       |
| MOD-AUD| Auditoría & Trazabilidad    | Registro y consulta de eventos relevantes para auditoría.              |

> Nota: algunos aspectos (por ejemplo, asignación de roles) están repartidos entre  
> `MOD-IA` y `MOD-EMP`, pero se los separa para clarificar el enfoque.

---

## 3. Módulo MOD-IA — Identidad & Acceso

### 3.1 Alcance funcional

Este módulo se encarga de:

- Permitir que un **empleado** inicie sesión en el sistema si está autorizado.
- Gestionar el estado básico de acceso:
  - Empleado ACTIVO vs BLOQUEADO.
- Asociar la sesión autenticada con:
  - Su identidad (`Empleado`).
  - Sus roles asociados (`Rol`).

No incluye (por ahora):

- Renovación de contraseñas.
- Gestión avanzada de sesiones, tokens o SSO.
- Autorización fina por recurso (se asume control por rol en la capa de aplicación).

### 3.2 Entidades principales

- `Empleado` (E1)
- `Rol` (E2)
- Relación N:M `Empleado-Rol` (tabla técnica)
- `EventoAuditoria` (E7) — para registrar logins exitosos/fallidos (en coordinación con MOD-AUD).

### 3.3 Casos de uso / User Stories relacionadas

- `US-AUT-001` — Iniciar sesión como empleado.
- RF relacionados (según `04-requisitos-funcionales.md`):
  - Autenticación básica de empleados.
  - Registro de intentos fallidos (vía auditoría).

### 3.4 Responsabilidades por capa

**Capa de Presentación (API/Web)**

- Endpoints típicos:
  - POST `/api/auth/login`
- Validar formato de entrada (usuario, contraseña).
- Devolver:
  - Indicación de éxito/fracaso.
  - Datos mínimos del empleado autenticado (ej.: nombre, roles) si el MVP lo exige.

**Capa de Aplicación**

- Servicio `AuthAppService` (nombre sugerido):
  - Verificar credenciales usando repositorios de `Empleado`.
  - Verificar estado del empleado (ACTIVO vs BLOQUEADO).
  - Registrar eventos de auditoría de login (éxito o fallo).
- Resolver a qué roles pertenece el empleado autenticado.

**Capa de Dominio**

- Entidad `Empleado`:
  - Representa usuario interno + estado (ACTIVO/BLOQUEADO).
- Entidad `Rol`:
  - Representa perfiles de acceso.
- Reglas:
  - Empleado BLOQUEADO no puede autenticarse.

**Capa de Infraestructura**

- Implementaciones de repositorios:
  - `EmpleadoRepository` (búsqueda por username).
  - `RolRepository` (si aplica).
- Integración con mecanismo de hash de contraseñas.

---

## 4. Módulo MOD-EMP — Gestión de Empleados

### 4.1 Alcance funcional

Este módulo se encarga de:

- Dar de alta empleados internos.
- Modificar datos básicos de empleados.
- Asignar o modificar roles.
- Bloquear empleados para que no puedan acceder al sistema.

### 4.2 Entidades principales

- `Empleado` (E1)
- `Rol` (E2)
- Relación `Empleado-Rol`
- `EventoAuditoria` (E7) — para eventos como “bloqueo de empleado”.

### 4.3 Casos de uso / User Stories relacionadas

- `US-EMP-001` — Dar de alta empleados.
- `US-EMP-002` — Asignar o modificar roles de empleados.
- `US-EMP-003` — Bloquear empleados.

### 4.4 Responsabilidades por capa

**Presentación**

- Endpoints:
  - POST `/api/empleados`
  - PUT `/api/empleados/{id}`
  - POST `/api/empleados/{id}/bloquear`
  - POST `/api/empleados/{id}/roles`
- Validaciones de request y respuesta (DTOs de empleado).

**Aplicación**

- `EmpleadoAppService`:
  - Crear nuevos empleados con estado ACTIVO.
  - Asignar roles a empleados existentes.
  - Bloquear empleados y generar `EventoAuditoria` asociado.

**Dominio**

- `Empleado`:
  - Comprueba reglas sencillas: no permitir cambio a BLOQUEADO si ya está BLOQUEADO, etc.
- `Rol`:
  - Modelo de roles.

**Infraestructura**

- `EmpleadoRepository` y `RolRepository`.
- Mapeo ORM (`Empleado`, `Rol`, tabla `Empleado_Rol`).

---

## 5. Módulo MOD-CLI — Gestión de Clientes

### 5.1 Alcance funcional

Este módulo se encarga de:

- Alta de clientes (persona física o jurídica).
- Actualización de datos principales.
- Bloqueo de clientes (cuando presenten riesgo, incumplimientos, etc.).

### 5.2 Entidades principales

- `Cliente` (E3)
- `EventoAuditoria` (E7) — para registrar bloqueos de clientes (con MOD-AUD).

### 5.3 Casos de uso / User Stories relacionadas

- `US-CLI-001` — Dar de alta clientes.
- `US-CLI-002` — Actualizar datos de clientes.
- `US-CLI-003` — Bloquear clientes.

### 5.4 Responsabilidades por capa

**Presentación**

- Endpoints:
  - POST `/api/clientes`
  - PUT `/api/clientes/{id}`
  - POST `/api/clientes/{id}/bloquear`
  - GET `/api/clientes/{id}`
  - GET `/api/clientes` (listado básico para el MVP si se desea)
- Validar:
  - Formato de documento.
  - Campos obligatorios.

**Aplicación**

- `ClienteAppService`:
  - Crear clientes nuevos (verificando unicidad de documento).
  - Actualizar datos (sin duplicar documento).
  - Bloquear clientes y coordinar impacto en otros módulos (ej.: apertura de cuentas restringida).

**Dominio**

- `Cliente`:
  - Reglas de unicidad de documento (apoyadas en infraestructura).
  - Estado ACTIVO/BLOQUEADO.

**Infraestructura**

- `ClienteRepository`.
- Consultas por documento, por ID, etc.

---

## 6. Módulo MOD-CTA — Gestión de Cuentas

### 6.1 Alcance funcional

Este módulo se encarga de:

- Apertura de cuentas bancarias para clientes existentes.
- Consulta de saldo de cuenta.
- Consulta de movimientos de cuenta (en coordinación con MOD-OP).

### 6.2 Entidades principales

- `CuentaBancaria` (E4)
- `Cliente` (E3)
- `MovimientoCuenta` (E5)

### 6.3 Casos de uso / User Stories relacionadas

- `US-CTA-001` — Abrir cuenta bancaria para cliente.
- `US-CTA-002` — Consultar saldo de cuenta.
- `US-CTA-003` — Consultar movimientos de cuenta.

### 6.4 Responsabilidades por capa

**Presentación**

- Endpoints:
  - POST `/api/cuentas`
  - GET `/api/cuentas/{id}/saldo`
  - GET `/api/cuentas/{id}/movimientos`
- Validar:
  - Que el clienteId venga informado al abrir cuenta.
  - Parámetros de paginación para movimientos (si se incluye).

**Aplicación**

- `CuentaAppService`:
  - Verificar que el cliente exista y esté ACTIVO.
  - Crear `CuentaBancaria` con tipo, moneda, saldo inicial 0.
  - Consultar saldo actual.
  - Consultar movimientos, posiblemente con filtros por fecha.

**Dominio**

- `CuentaBancaria`:
  - Mantener estado (ACTIVA, BLOQUEADA, CERRADA).
  - Mantener y exponer `saldoActual`.
- `MovimientoCuenta`:
  - Historial de movimientos ligados a una cuenta.

**Infraestructura**

- `CuentaRepository`.
- `MovimientoRepository`.
- Consultas eficientes para:
  - Saldo actual.
  - Lista de movimientos por cuenta y rango de fechas.

---

## 7. Módulo MOD-OP — Operaciones Financieras

### 7.1 Alcance funcional

Este módulo se encarga de:

- Registrar **depósitos** en cuentas.
- Registrar **extracciones**.
- Registrar **transferencias internas** entre cuentas del mismo banco.

Apoyándose en:

- `CuentaBancaria` (E4).
- `MovimientoCuenta` (E5).
- `OperacionFinanciera` (E6).
- `Empleado` (E1).

### 7.2 Entidades principales

- `OperacionFinanciera` (E6)
- `MovimientoCuenta` (E5)
- `CuentaBancaria` (E4)
- `Empleado` (E1)
- `EventoAuditoria` (E7) — para registro de operaciones (junto con MOD-AUD).

### 7.3 Casos de uso / User Stories relacionadas

- `US-OP-001` — Registrar depósito en cuenta.
- `US-OP-002` — Registrar extracción de cuenta.
- `US-OP-003` — Registrar transferencia interna entre cuentas.

### 7.4 Responsabilidades por capa

**Presentación**

- Endpoints:
  - POST `/api/operaciones/depositos`
  - POST `/api/operaciones/extracciones`
  - POST `/api/operaciones/transferencias`
- Validar:
  - Que los montos sean positivos.
  - Que las cuentas origen/destino estén informadas cuando corresponda.

**Aplicación**

- `OperacionAppService`:
  - Cargar cuentas origen/destino desde repositorios.
  - Verificar estados de cuentas (ACTIVAS).
  - Verificar que origen ≠ destino en transferencias.
  - Coordinar:
    - Creación de `OperacionFinanciera`.
    - Creación de `MovimientoCuenta` asociados.
    - Actualización de saldos.
    - Registro de `EventoAuditoria`.
  - Manejar transacciones (todo o nada).

**Dominio**

- `OperacionFinanciera`:
  - Métodos fábrica, por ejemplo:
    - `crearDeposito(cuentaDestino, monto, empleado)`
    - `crearExtraccion(cuentaOrigen, monto, empleado)`
    - `crearTransferencia(cuentaOrigen, cuentaDestino, monto, empleado)`
- `CuentaBancaria`:
  - Métodos como:
    - `registrarCredito(monto)`
    - `registrarDebito(monto)` (verificando saldo suficiente).
- `MovimientoCuenta`:
  - Representar cada débito/crédito con sus reglas.

**Infraestructura**

- `OperacionRepository`.
- `MovimientoRepository`.
- Mecanismos de persistencia transaccional (por ejemplo, JPA + transacciones).

---

## 8. Módulo MOD-AUD — Auditoría & Trazabilidad

### 8.1 Alcance funcional

Este módulo se encarga de:

- Registrar eventos relevantes para auditoría:
  - Logins exitosos/fallidos.
  - Bloqueo de empleados.
  - Bloqueo de clientes.
  - Operaciones financieras registradas.
- Permitir consultas básicas sobre estos eventos (al menos a nivel de administrador).

### 8.2 Entidades principales

- `EventoAuditoria` (E7)
- `Empleado` (E1) — actor asociado a ciertos eventos.
- `OperacionFinanciera` (E6) — para eventos de operaciones.

### 8.3 Casos de uso / User Stories relacionadas

- `US-AUD-001` — Consultar trazabilidad de operaciones.

### 8.4 Responsabilidades por capa

**Presentación**

- Endpoints (orientativos, para el MVP o versiones posteriores):
  - GET `/api/auditoria/eventos`
  - GET `/api/auditoria/eventos?tipo=...&fechaDesde=...&fechaHasta=...`
- Solo accesibles para roles con permisos adecuados (ej.: ADMIN).

**Aplicación**

- `AuditoriaAppService`:
  - Registrar eventos:
    - Desde otros servicios (Auth, Empleados, Clientes, Operaciones).
  - Exponer métodos de consulta por:
    - Tipo de evento.
    - Rango de fechas.
    - Empleado.
    - Entidad afectada.

**Dominio**

- `EventoAuditoria`:
  - Modelo de eventos.
  - Posible lógica básica, como validación de tipo de evento.

**Infraestructura**

- `EventoAuditoriaRepository`.
- Consultas eficientes por criterios de búsqueda.

---

## 9. Tabla resumen módulos ↔ entidades ↔ capas

| Módulo  | Entidades clave                                                | Capas más activas                          |
|---------|----------------------------------------------------------------|--------------------------------------------|
| MOD-IA  | Empleado, Rol, EventoAuditoria                                 | Presentación, Aplicación, Dominio, Infra   |
| MOD-EMP | Empleado, Rol, EventoAuditoria                                 | Presentación, Aplicación, Dominio, Infra   |
| MOD-CLI | Cliente, EventoAuditoria                                       | Presentación, Aplicación, Dominio, Infra   |
| MOD-CTA | CuentaBancaria, Cliente, MovimientoCuenta                      | Presentación, Aplicación, Dominio, Infra   |
| MOD-OP  | OperacionFinanciera, MovimientoCuenta, CuentaBancaria, Empleado, EventoAuditoria | Presentación, Aplicación, Dominio, Infra |
| MOD-AUD | EventoAuditoria, Empleado, OperacionFinanciera                 | Aplicación, Dominio, Infra, (Presentación para consultas) |

---

## 10. Próximos pasos

Con los módulos y sus responsabilidades definidos, los siguientes pasos naturales son:

1. Definir **endpoints concretos y contratos de API** (inputs/outputs) por módulo  
   en un documento específico (por ejemplo, `12-endpoints-api-rest.md`).
2. Aterrizar la estructura de paquetes/clases del proyecto real para reflejar:
   - Capas (`presentation`, `application`, `domain`, `infrastructure`).  
   - Módulos (`empleados`, `clientes`, `cuentas`, `operaciones`, `auditoria`, `auth`).
3. A partir de estos módulos, crear:
   - Issues en GitHub por módulo + caso de uso.  
   - Tareas técnicas específicas (endpoints, servicios, repositorios, tests).

Este documento ayuda a mantener una **visión modular y cohesiva** del sistema bancario educativo,
evitando mezclar responsabilidades y facilitando la evolución futura del proyecto.
