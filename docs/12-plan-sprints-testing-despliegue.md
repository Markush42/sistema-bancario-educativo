# 12 — Endpoints API REST del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define los **endpoints principales de la API REST** del sistema bancario educativo,
alineados con:

- Los **módulos y responsabilidades** (`11-modulos-responsabilidades.md`).
- La **arquitectura en capas** (`10-arquitectura-capas.md`).
- El **modelo conceptual** y las entidades de dominio (`07`, `08`, `09`).

El objetivo es:

- Tener una vista clara de **qué endpoints expone el backend**.
- Qué hace cada endpoint, **qué recibe** y **qué devuelve**.
- Qué **módulo** y **caso de uso** cubre cada uno.

Este documento describe el **contrato HTTP** a alto nivel para el MVP; los detalles
de validaciones y formatos pueden refinarse en etapas posteriores.

---

## 2. Convenciones generales

- URL base recomendada: `/api`
- Formato de datos:
  - Requests y responses en **JSON**.
- Autenticación:
  - Para el MVP se asume autenticación básica por sesión/empleado autenticado.
  - En una versión futura podría integrarse JWT u otro mecanismo.
- Convención de códigos HTTP (resumen):
  - 200 OK → Operación exitosa (GET, PUT, POST sin creación).
  - 201 Created → Recurso creado exitosamente.
  - 400 Bad Request → Request inválida (datos faltantes o mal formados).
  - 401 Unauthorized → Falta autenticación.
  - 403 Forbidden → El empleado autenticado no tiene permisos.
  - 404 Not Found → Recurso no encontrado.
  - 409 Conflict → Conflictos de negocio (ej.: saldo insuficiente, duplicado).
  - 500 Internal Server Error → Error interno no previsto.

---

## 3. Módulo MOD-IA — Identidad & Acceso

### 3.1 Login de empleados

- Método: `POST`
- Path: `/api/auth/login`
- Descripción: Autentica a un empleado con `username` y `password`.

Request (JSON):

    {
      "username": "cajero01",
      "password": "secreto123"
    }

Response (200 OK):

    {
      "empleadoId": 123,
      "username": "cajero01",
      "nombre": "Juan",
      "apellido": "Pérez",
      "roles": ["CAJERO", "OFICIAL_CUENTA"]
    }

Errores típicos:

- 401 Unauthorized → credenciales inválidas.
- 403 Forbidden → empleado BLOQUEADO.
- 400 Bad Request → faltan campos.

---

## 4. Módulo MOD-EMP — Gestión de Empleados

### 4.1 Alta de empleado

- Método: `POST`
- Path: `/api/empleados`
- Descripción: Crear un nuevo empleado con uno o varios roles.

Request:

    {
      "username": "cajero02",
      "password": "secreto123",
      "nombre": "Ana",
      "apellido": "García",
      "email": "ana.garcia@sistema.com",
      "roles": ["CAJERO"]
    }

Response (201 Created):

    {
      "id": 124,
      "username": "cajero02",
      "nombre": "Ana",
      "apellido": "García",
      "email": "ana.garcia@sistema.com",
      "estado": "ACTIVO",
      "roles": ["CAJERO"]
    }

Roles requeridos (sugerido): `ADMIN`.

---

### 4.2 Modificación de datos de empleado

- Método: `PUT`
- Path: `/api/empleados/{id}`
- Descripción: Actualizar datos básicos (no credenciales) de un empleado.

Request:

    {
      "nombre": "Ana María",
      "apellido": "García",
      "email": "ana.m.garcia@sistema.com"
    }

Response (200 OK):

    {
      "id": 124,
      "username": "cajero02",
      "nombre": "Ana María",
      "apellido": "García",
      "email": "ana.m.garcia@sistema.com",
      "estado": "ACTIVO",
      "roles": ["CAJERO"]
    }

---

### 4.3 Bloqueo de empleado

- Método: `POST`
- Path: `/api/empleados/{id}/bloquear`
- Descripción: Cambia el estado de un empleado a BLOQUEADO.

Response (200 OK):

    {
      "id": 124,
      "estadoAnterior": "ACTIVO",
      "estadoActual": "BLOQUEADO"
    }

Efectos colaterales:

- Debe registrarse un `EventoAuditoria` asociado.

---

### 4.4 Asignación de roles a empleado

- Método: `POST`
- Path: `/api/empleados/{id}/roles`
- Descripción: Reemplaza o ajusta la lista de roles asignados al empleado.

Request:

    {
      "roles": ["CAJERO", "OFICIAL_CUENTA"]
    }

Response (200 OK):

    {
      "id": 124,
      "roles": ["CAJERO", "OFICIAL_CUENTA"]
    }

---

## 5. Módulo MOD-CLI — Gestión de Clientes

### 5.1 Alta de cliente

- Método: `POST`
- Path: `/api/clientes`
- Descripción: Crear un cliente (físico o jurídico).

Request (persona física):

    {
      "tipoPersona": "FISICA",
      "nombre": "Marcos",
      "apellido": "Álvarez",
      "tipoDocumento": "DNI",
      "numeroDocumento": "30123456",
      "email": "marcos@example.com",
      "telefono": "1122334455",
      "direccion": "Calle Falsa 123"
    }

Response (201 Created):

    {
      "id": 2001,
      "tipoPersona": "FISICA",
      "nombre": "Marcos",
      "apellido": "Álvarez",
      "tipoDocumento": "DNI",
      "numeroDocumento": "30123456",
      "estado": "ACTIVO"
    }

---

### 5.2 Actualización de datos de cliente

- Método: `PUT`
- Path: `/api/clientes/{id}`
- Descripción: Actualizar datos de contacto del cliente.

Request:

    {
      "email": "marcos.alvarez@example.com",
      "telefono": "1199887766",
      "direccion": "Av. Siempre Viva 742"
    }

Response (200 OK):

    {
      "id": 2001,
      "tipoPersona": "FISICA",
      "nombre": "Marcos",
      "apellido": "Álvarez",
      "tipoDocumento": "DNI",
      "numeroDocumento": "30123456",
      "email": "marcos.alvarez@example.com",
      "telefono": "1199887766",
      "direccion": "Av. Siempre Viva 742",
      "estado": "ACTIVO"
    }

---

### 5.3 Bloqueo de cliente

- Método: `POST`
- Path: `/api/clientes/{id}/bloquear`
- Descripción: Pone el cliente en estado BLOQUEADO (no puede abrir nuevas cuentas ni operar).

Response (200 OK):

    {
      "id": 2001,
      "estadoAnterior": "ACTIVO",
      "estadoActual": "BLOQUEADO"
    }

---

### 5.4 Consulta de cliente por ID

- Método: `GET`
- Path: `/api/clientes/{id}`
- Descripción: Devuelve datos básicos de un cliente.

Response (200 OK):

    {
      "id": 2001,
      "tipoPersona": "FISICA",
      "nombre": "Marcos",
      "apellido": "Álvarez",
      "tipoDocumento": "DNI",
      "numeroDocumento": "30123456",
      "estado": "ACTIVO"
    }

---

## 6. Módulo MOD-CTA — Gestión de Cuentas

### 6.1 Apertura de cuenta bancaria

- Método: `POST`
- Path: `/api/cuentas`
- Descripción: Abrir una nueva cuenta bancaria para un cliente existente.

Request:

    {
      "clienteId": 2001,
      "tipoCuenta": "CAJA_AHORRO",
      "moneda": "ARS"
    }

Response (201 Created):

    {
      "id": 3001,
      "numeroCuenta": "000123-0003001",
      "clienteId": 2001,
      "tipoCuenta": "CAJA_AHORRO",
      "moneda": "ARS",
      "saldoActual": 0.0,
      "estado": "ACTIVA",
      "fechaApertura": "2025-11-26T12:34:56"
    }

---

### 6.2 Consultar saldo de cuenta

- Método: `GET`
- Path: `/api/cuentas/{id}/saldo`
- Descripción: Devuelve el saldo actual de la cuenta.

Response (200 OK):

    {
      "cuentaId": 3001,
      "numeroCuenta": "000123-0003001",
      "saldoActual": 15000.75,
      "moneda": "ARS",
      "estado": "ACTIVA"
    }

---

### 6.3 Consultar movimientos de cuenta

- Método: `GET`
- Path: `/api/cuentas/{id}/movimientos`
- Descripción: Lista de movimientos de la cuenta, opcionalmente filtrados por fecha.

Parámetros de query (opcionales):

- `fechaDesde` (ej.: `2025-11-01`)
- `fechaHasta` (ej.: `2025-11-30`)
- `page`, `size` (si se agrega paginación)

Response (200 OK):

    {
      "cuentaId": 3001,
      "numeroCuenta": "000123-0003001",
      "movimientos": [
        {
          "idMovimiento": 5001,
          "fechaHora": "2025-11-25T10:30:00",
          "tipoMovimiento": "CREDITO",
          "monto": 10000.00,
          "saldoPosterior": 10000.00,
          "descripcion": "Depósito en ventanilla"
        },
        {
          "idMovimiento": 5002,
          "fechaHora": "2025-11-26T09:00:00",
          "tipoMovimiento": "CREDITO",
          "monto": 5000.75,
          "saldoPosterior": 15000.75,
          "descripcion": "Transferencia recibida"
        }
      ]
    }

---

## 7. Módulo MOD-OP — Operaciones Financieras

### 7.1 Registrar depósito

- Método: `POST`
- Path: `/api/operaciones/depositos`
- Descripción: Registrar un depósito en una cuenta.

Request:

    {
      "cuentaDestinoId": 3001,
      "monto": 1000.00,
      "descripcion": "Depósito en efectivo"
    }

Response (201 Created):

    {
      "operacionId": 7001,
      "tipoOperacion": "DEPOSITO",
      "cuentaDestinoId": 3001,
      "montoTotal": 1000.00,
      "fechaHora": "2025-11-26T11:00:00",
      "movimientosGenerados": [
        {
          "idMovimiento": 5003,
          "tipoMovimiento": "CREDITO",
          "monto": 1000.00,
          "saldoPosterior": 16000.75
        }
      ]
    }

---

### 7.2 Registrar extracción

- Método: `POST`
- Path: `/api/operaciones/extracciones`
- Descripción: Registrar una extracción de una cuenta.

Request:

    {
      "cuentaOrigenId": 3001,
      "monto": 2000.00,
      "descripcion": "Extracción en caja"
    }

Response (201 Created):

    {
      "operacionId": 7002,
      "tipoOperacion": "EXTRACCION",
      "cuentaOrigenId": 3001,
      "montoTotal": 2000.00,
      "fechaHora": "2025-11-26T12:00:00",
      "movimientosGenerados": [
        {
          "idMovimiento": 5004,
          "tipoMovimiento": "DEBITO",
          "monto": 2000.00,
          "saldoPosterior": 14000.75
        }
      ]
    }

Errores típicos:

- 409 Conflict → saldo insuficiente.
- 400 Bad Request → monto no válido, cuenta no válida.

---

### 7.3 Registrar transferencia interna

- Método: `POST`
- Path: `/api/operaciones/transferencias`
- Descripción: Registrar una transferencia interna entre dos cuentas del banco.

Request:

    {
      "cuentaOrigenId": 3001,
      "cuentaDestinoId": 3002,
      "monto": 500.00,
      "descripcion": "Transferencia interna"
    }

Response (201 Created):

    {
      "operacionId": 7003,
      "tipoOperacion": "TRANSFERENCIA_INTERNA",
      "cuentaOrigenId": 3001,
      "cuentaDestinoId": 3002,
      "montoTotal": 500.00,
      "fechaHora": "2025-11-26T12:30:00",
      "movimientosGenerados": [
        {
          "idMovimiento": 5005,
          "cuentaId": 3001,
          "tipoMovimiento": "DEBITO",
          "monto": 500.00,
          "saldoPosterior": 13500.75
        },
        {
          "idMovimiento": 5006,
          "cuentaId": 3002,
          "tipoMovimiento": "CREDITO",
          "monto": 500.00,
          "saldoPosterior": 500.00
        }
      ]
    }

---

## 8. Módulo MOD-AUD — Auditoría & Trazabilidad

### 8.1 Listar eventos de auditoría

- Método: `GET`
- Path: `/api/auditoria/eventos`
- Descripción: Lista eventos de auditoría, filtrables por tipo, fechas y empleado.

Parámetros de query (opcionales):

- `tipoEvento` (ej.: `LOGIN_EXITOSO`, `LOGIN_FALLIDO`, `OPERACION_FINANCIERA_REGISTRADA`)
- `empleadoId`
- `fechaDesde`
- `fechaHasta`
- `page`, `size` (opcional para paginación)

Response (200 OK):

    {
      "eventos": [
        {
          "idEvento": 9001,
          "tipoEvento": "LOGIN_EXITOSO",
          "fechaHora": "2025-11-26T08:00:00",
          "empleadoId": 123,
          "entidadTipo": null,
          "entidadId": null,
          "detalle": "Login desde 192.168.0.10"
        },
        {
          "idEvento": 9002,
          "tipoEvento": "OPERACION_FINANCIERA_REGISTRADA",
          "fechaHora": "2025-11-26T11:00:00",
          "empleadoId": 123,
          "entidadTipo": "OPERACION_FINANCIERA",
          "entidadId": 7001,
          "detalle": "Depósito en cuenta 3001"
        }
      ]
    }

Roles requeridos (sugerido): `ADMIN`.

---

## 9. Resumen de endpoints del MVP

Resumen tabular por módulo:

| Módulo | Método | Path                              | Descripción                              |
|--------|--------|-----------------------------------|------------------------------------------|
| MOD-IA | POST   | `/api/auth/login`                | Login de empleados                       |
| MOD-EMP| POST   | `/api/empleados`                 | Alta de empleado                         |
| MOD-EMP| PUT    | `/api/empleados/{id}`            | Modificar empleado                       |
| MOD-EMP| POST   | `/api/empleados/{id}/bloquear`   | Bloquear empleado                        |
| MOD-EMP| POST   | `/api/empleados/{id}/roles`      | Asignar roles                            |
| MOD-CLI| POST   | `/api/clientes`                  | Alta de cliente                          |
| MOD-CLI| PUT    | `/api/clientes/{id}`             | Actualizar cliente                       |
| MOD-CLI| POST   | `/api/clientes/{id}/bloquear`    | Bloquear cliente                         |
| MOD-CLI| GET    | `/api/clientes/{id}`             | Consultar cliente                        |
| MOD-CTA| POST   | `/api/cuentas`                   | Apertura de cuenta                       |
| MOD-CTA| GET    | `/api/cuentas/{id}/saldo`        | Consultar saldo de cuenta                |
| MOD-CTA| GET    | `/api/cuentas/{id}/movimientos`  | Consultar movimientos de cuenta          |
| MOD-OP | POST   | `/api/operaciones/depositos`     | Registrar depósito                       |
| MOD-OP | POST   | `/api/operaciones/extracciones`  | Registrar extracción                     |
| MOD-OP | POST   | `/api/operaciones/transferencias`| Registrar transferencia interna          |
| MOD-AUD| GET    | `/api/auditoria/eventos`         | Listar eventos de auditoría              |

---

## 10. Notas finales

- Estos endpoints cubren el **MVP educativo** definido; en futuras versiones se pueden extender:
  - Parámetros de paginación y ordenamiento.
  - Filtros adicionales (por cliente, por tipo de cuenta, etc.).
  - Mecanismos de autenticación/seguridad más avanzados (JWT, OAuth2, etc.).
- Antes de implementar, se recomienda:
  - Crear issues en GitHub por endpoint o por grupo de endpoints.
  - Vincular cada endpoint a:
    - Un módulo (`MOD-IA`, `MOD-EMP`, etc.).
    - Una o más user stories (`06-user-stories.md`).
