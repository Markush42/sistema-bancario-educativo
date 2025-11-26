# 08 — Detalle de Entidades del Dominio (Atributos y Tipos)

## 1. Objetivo del documento

En este documento se detallan los **atributos principales** de cada entidad de dominio definida
en `07-entidades-dominio.md`, incluyendo:

- Nombre del atributo.
- Tipo de dato sugerido (a nivel lógico, agnóstico de implementación).
- Obligatoriedad.
- Descripción funcional.
- Reglas o restricciones relevantes.

Los tipos sugeridos son genéricos (pueden mapearse luego a tipos concretos de:
Java, SQL, etc.):

- `String`
- `Integer`
- `Long`
- `Decimal`
- `Boolean`
- `Date` (solo fecha)
- `DateTime` (fecha y hora)
- `Enum<...>` (enumeraciones de valores limitados)
- `UUID` (identificador único global, opcionalmente)

---

## 2. Enumeraciones del dominio

Antes de detallar las entidades, se definen las **enumeraciones** utilizadas.

### 2.1 Enum EstadoEmpleado

Valores posibles:

- `ACTIVO`
- `BLOQUEADO`

### 2.2 Enum EstadoCliente

Valores posibles:

- `ACTIVO`
- `BLOQUEADO`

### 2.3 Enum EstadoCuenta

Valores posibles:

- `ACTIVA`
- `BLOQUEADA`
- `CERRADA`

### 2.4 Enum TipoCuenta

Valores posibles (MVP):

- `CAJA_AHORRO`
- `CUENTA_CORRIENTE`

### 2.5 Enum TipoMovimiento

Valores posibles:

- `DEBITO`   (disminuye saldo)
- `CREDITO`  (aumenta saldo)

### 2.6 Enum TipoOperacion

Valores posibles (MVP):

- `DEPOSITO`
- `EXTRACCION`
- `TRANSFERENCIA_INTERNA`

### 2.7 Enum TipoEventoAuditoria

Ejemplos de valores posibles (ampliables):

- `LOGIN_EXITOSO`
- `LOGIN_FALLIDO`
- `BLOQUEO_EMPLEADO`
- `BLOQUEO_CLIENTE`
- `OPERACION_FINANCIERA_REGISTRADA`

---

## 3. Entidad Empleado (E1)

Representa a un usuario interno que opera el sistema (cajero, oficial, administrador).

### 3.1 Atributos principales de Empleado

| Atributo          | Tipo              | Obligatorio | Descripción                                                                 |
|-------------------|-------------------|-------------|-----------------------------------------------------------------------------|
| idEmpleado        | Long / UUID       | Sí          | Identificador único del empleado en el sistema.                             |
| username          | String            | Sí          | Nombre de usuario para autenticación. Debe ser único.                        |
| hashPassword      | String            | Sí          | Contraseña almacenada de forma hasheada (no en texto plano).                |
| nombre            | String            | Sí          | Nombre del empleado.                                                        |
| apellido          | String            | Sí          | Apellido del empleado.                                                      |
| email             | String            | No (MVP)    | Correo electrónico corporativo del empleado.                                |
| estado            | Enum<EstadoEmpleado> | Sí       | Estado del empleado: ACTIVO o BLOQUEADO.                                    |
| fechaAlta         | DateTime          | Sí          | Fecha y hora en que se dio de alta el empleado.                             |
| fechaBaja         | DateTime          | No          | Fecha y hora en que se dio de baja (si aplica).                             |
| ultimoLoginAt     | DateTime          | No          | Fecha y hora del último inicio de sesión exitoso.                           |

> **Nota:** la relación Empleado–Rol (N:M) se modelará mediante una tabla de relación técnica
> (por ejemplo `empleado_rol`) en el diseño físico, no como entidad de negocio aparte.

---

## 4. Entidad Rol (E2)

Define un rol asignable a empleados (ADMIN, CAJERO, OFICIAL_CUENTA, etc.).

### 4.1 Atributos principales de Rol

| Atributo     | Tipo        | Obligatorio | Descripción                                                   |
|--------------|------------|-------------|---------------------------------------------------------------|
| idRol        | Long       | Sí          | Identificador único del rol.                                  |
| nombre       | String      | Sí          | Nombre del rol (p.ej.: `ADMIN`, `CAJERO`, `OFICIAL_CUENTA`).  |
| descripcion  | String      | No          | Descripción funcional del rol.                                |
| activo       | Boolean     | Sí          | Indica si el rol está disponible para asignación.             |

---

## 5. Entidad Cliente (E3)

Representa a la persona (física o jurídica) titular de cuentas.

### 5.1 Atributos principales de Cliente

| Atributo          | Tipo               | Obligatorio | Descripción                                                                 |
|-------------------|--------------------|-------------|-----------------------------------------------------------------------------|
| idCliente         | Long / UUID        | Sí          | Identificador único del cliente.                                            |
| tipoPersona       | String / Enum      | Sí          | Tipo de persona (ej.: `FISICA`, `JURIDICA`).                                |
| nombre            | String             | Condicional | Nombre del cliente (para persona física).                                   |
| apellido          | String             | Condicional | Apellido del cliente (para persona física).                                 |
| razonSocial       | String             | Condicional | Razón social (para persona jurídica).                                       |
| tipoDocumento     | String / Enum      | Sí          | Tipo de documento (DNI, CUIT, PASAPORTE, etc.).                             |
| numeroDocumento   | String             | Sí          | Número de documento. Debe ser único en el sistema.                          |
| email             | String             | No          | Correo electrónico de contacto.                                             |
| telefono          | String             | No          | Teléfono de contacto.                                                       |
| direccion         | String             | No          | Dirección principal (texto libre en el MVP).                                |
| estado            | Enum<EstadoCliente>| Sí          | Estado del cliente: ACTIVO o BLOQUEADO.                                     |
| fechaAlta         | DateTime           | Sí          | Fecha y hora de alta del cliente.                                           |
| fechaBaja         | DateTime           | No          | Fecha y hora de baja o cierre de relación (si aplica).                      |

> **Regla:** la combinación (`tipoDocumento`, `numeroDocumento`) debe ser única para todo cliente.

---

## 6. Entidad CuentaBancaria (E4)

Representa una cuenta bancaria asociada a un cliente.

### 6.1 Atributos principales de CuentaBancaria

| Atributo          | Tipo                   | Obligatorio | Descripción                                                                 |
|-------------------|------------------------|-------------|-----------------------------------------------------------------------------|
| idCuenta          | Long / UUID            | Sí          | Identificador interno único de la cuenta.                                   |
| numeroCuenta      | String                 | Sí          | Número de cuenta visible para el banco/cliente. Debe ser único.             |
| clienteId         | Long / UUID            | Sí          | Referencia al cliente titular (`Cliente.idCliente`).                         |
| tipoCuenta        | Enum<TipoCuenta>       | Sí          | Tipo de cuenta (CAJA_AHORRO, CUENTA_CORRIENTE, etc.).                        |
| moneda            | String / Enum          | No (MVP)    | Moneda de la cuenta (ej.: `ARS`). En el MVP puede fijarse a una sola moneda.|
| saldoActual       | Decimal                | Sí          | Saldo actual de la cuenta.                                                  |
| estado            | Enum<EstadoCuenta>     | Sí          | Estado de la cuenta (ACTIVA, BLOQUEADA, CERRADA).                           |
| fechaApertura     | DateTime               | Sí          | Fecha y hora en que se abrió la cuenta.                                     |
| fechaCierre       | DateTime               | No          | Fecha y hora de cierre (si la cuenta está CERRADA).                         |
| descripcion       | String                 | No          | Observaciones o descripción adicional (opcional).                            |

> **Nota:** aunque el saldo podría derivarse de los movimientos, se almacena `saldoActual`
> por eficiencia y se mantiene consistente mediante las reglas de negocio y transacciones.

---

## 7. Entidad MovimientoCuenta (E5)

Representa un movimiento financiero que afecta el saldo de una cuenta.

### 7.1 Atributos principales de MovimientoCuenta

| Atributo            | Tipo                     | Obligatorio | Descripción                                                                 |
|---------------------|--------------------------|-------------|-----------------------------------------------------------------------------|
| idMovimiento        | Long / UUID              | Sí          | Identificador único del movimiento.                                         |
| cuentaId            | Long / UUID              | Sí          | Referencia a la cuenta afectada (`CuentaBancaria.idCuenta`).               |
| tipoMovimiento      | Enum<TipoMovimiento>     | Sí          | Tipo de movimiento: DEBITO o CREDITO.                                      |
| monto               | Decimal                  | Sí          | Monto del movimiento. Debe ser > 0.                                        |
| saldoPosterior      | Decimal                  | Sí          | Saldo de la cuenta luego de aplicar este movimiento.                        |
| fechaHora           | DateTime                 | Sí          | Fecha y hora en que se registró el movimiento.                              |
| descripcion         | String                   | No          | Detalle o concepto del movimiento.                                          |
| empleadoId          | Long / UUID              | Sí          | Empleado que registró el movimiento (`Empleado.idEmpleado`).               |
| operacionId         | Long / UUID              | No          | Referencia a la operación lógica (`OperacionFinanciera.idOperacion`).      |

> **Regla:** el `saldoPosterior` debe ser coherente con el saldo anterior de la cuenta y el `monto`
> según el `tipoMovimiento` (DEBITO disminuye, CREDITO aumenta).

---

## 8. Entidad OperacionFinanciera (E6)

Representa una operación lógica (depósito, extracción, transferencia interna) que puede generar
uno o más movimientos de cuenta.

### 8.1 Atributos principales de OperacionFinanciera

| Atributo          | Tipo                     | Obligatorio | Descripción                                                                 |
|-------------------|--------------------------|-------------|-----------------------------------------------------------------------------|
| idOperacion       | Long / UUID              | Sí          | Identificador único de la operación financiera.                             |
| tipoOperacion     | Enum<TipoOperacion>      | Sí          | Tipo de operación (DEPOSITO, EXTRACCION, TRANSFERENCIA_INTERNA).           |
| fechaHora         | DateTime                 | Sí          | Fecha y hora de la operación.                                              |
| empleadoId        | Long / UUID              | Sí          | Empleado que ejecutó la operación (`Empleado.idEmpleado`).                 |
| cuentaOrigenId    | Long / UUID              | No          | Cuenta origen (usada en EXTRACCION y TRANSFERENCIA_INTERNA).               |
| cuentaDestinoId   | Long / UUID              | No          | Cuenta destino (usada en DEPOSITO y TRANSFERENCIA_INTERNA).                |
| montoTotal        | Decimal                  | Sí          | Monto total involucrado en la operación.                                   |
| descripcion       | String                   | No          | Descripción general o concepto de la operación.                            |
| canal             | String                   | No          | Canal por el que se realizó (ej.: `SUCURSAL`, `INTERNO`).                   |

> **Notas de diseño:**
> - En una **transferencia interna**, `cuentaOrigenId` y `cuentaDestinoId` deben estar presentes.
> - En un **depósito**, podría usarse solo `cuentaDestinoId`.
> - En una **extracción**, podría usarse solo `cuentaOrigenId`.
> - Los `MovimientoCuenta` asociados referencian esta operación mediante `operacionId`.

---

## 9. Entidad EventoAuditoria (E7)

Representa un evento relevante a nivel de auditoría (no solo financiero).

### 9.1 Atributos principales de EventoAuditoria

| Atributo          | Tipo                      | Obligatorio | Descripción                                                                 |
|-------------------|---------------------------|-------------|-----------------------------------------------------------------------------|
| idEvento          | Long / UUID               | Sí          | Identificador único del evento de auditoría.                                |
| tipoEvento        | Enum<TipoEventoAuditoria> | Sí          | Tipo de evento (LOGIN_EXITOSO, LOGIN_FALLIDO, etc.).                        |
| fechaHora         | DateTime                  | Sí          | Fecha y hora del evento.                                                    |
| empleadoId        | Long / UUID               | No          | Empleado asociado al evento (si aplica).                                    |
| entidadTipo       | String                    | No          | Tipo de entidad afectada (ej.: `CLIENTE`, `CUENTA`, `EMPLEADO`, etc.).      |
| entidadId         | Long / String             | No          | Identificador de la entidad afectada (según `entidadTipo`).                 |
| detalle           | String                    | No          | Descripción detallada del evento.                                           |
| origen            | String                    | No          | Información adicional de origen (IP, host, canal, etc.).                    |

> **Ejemplos:**
> - En un `LOGIN_FALLIDO`, `empleadoId` puede no existir si el usuario no es reconocido.
> - En un `BLOQUEO_CLIENTE`, `entidadTipo = 'CLIENTE'` y `entidadId` apunta al cliente bloqueado.

---

## 10. Notas generales de diseño para el MVP

- Todos los `id` pueden implementarse como `Long` autoincremental o como `UUID`,
  según las decisiones técnicas del backend.
- Los campos de fecha/hora (`DateTime`) deben almacenarse en un formato consistente
  (idealmente UTC a nivel técnico, mostrando horarios locales en la capa de presentación).
- En el diseño físico (base de datos) se definirán:
  - Claves primarias.
  - Claves foráneas entre entidades.
  - Índices en campos de búsqueda frecuente (documento de cliente, número de cuenta, etc.).

Este documento será la base directa para:

- El **modelo conceptual/UML** en `09-modelo-conceptual.md`.
- El diseño de tablas SQL.
- Las clases de entidad en la implementación del backend.
