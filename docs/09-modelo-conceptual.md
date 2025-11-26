# 09 — Modelo Conceptual del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento describe el **modelo conceptual** del sistema bancario educativo, a partir de:

- Las **entidades de dominio** definidas en `07-entidades-dominio.md`.
- El **detalle de atributos** definido en `08-entidades-detalle.md`.
- Los **requisitos funcionales** (`04-requisitos-funcionales.md`) y **user stories** (`06-user-stories.md`).

El modelo conceptual se centra en:

- **Qué entidades existen** en el dominio bancario del MVP.
- **Cómo se relacionan** entre sí (cardinalidades, dependencias).
- **Qué restricciones de integridad** deben respetarse a nivel de negocio.

No entra en detalles físicos (tipos concretos SQL, índices, etc.); eso quedará para el diseño
lógico/físico de la base de datos y las clases de entidad.

---

## 2. Notación y convenciones

- Cada entidad se nombra con **CamelCase**: `Empleado`, `Cliente`, `CuentaBancaria`, etc.
- Las principales relaciones se describen con:
  - `1` o `1..1` → exactamente una instancia.
  - `0..1` → cero o una instancia.
  - `0..*` → cero o muchas.
  - `1..*` → una o muchas.
- En las tablas de relaciones, se usa el formato:

  > `A (cardinalidad) — (cardinalidad) B`

  Por ejemplo: `Cliente (1) — (1..*) CuentaBancaria`.

---

## 3. Vista general del modelo

### 3.1 Descripción conceptual (visión textual)

A alto nivel, el modelo conceptual se puede describir así:

- Un **Empleado**:
  - Tiene uno o varios **Roles**.
  - Puede ejecutar muchas **OperacionesFinancieras**.
  - Puede registrar muchos **MovimientosCuenta**.
  - Puede originar muchos **EventosAuditoria**.

- Un **Cliente**:
  - Puede tener una o varias **CuentasBancarias**.

- Una **CuentaBancaria**:
  - Pertenece a un único **Cliente**.
  - Registra muchos **MovimientosCuenta**.

- Una **OperacionFinanciera**:
  - Es ejecutada por un único **Empleado**.
  - Puede generar uno o varios **MovimientosCuenta**.
  - Puede estar asociada a uno o varios **EventosAuditoria**.

- Un **MovimientoCuenta**:
  - Pertenece a una única **CuentaBancaria**.
  - Está asociado a una única **OperacionFinanciera** (en el modelo lógico del MVP).
  - Está registrado por un único **Empleado**.

- Un **EventoAuditoria**:
  - Puede estar asociado a un **Empleado** (quién generó el evento, si aplica).
  - Puede estar asociado a una **OperacionFinanciera** o a otra entidad afectada.

---

## 4. Submodelo: Identidad & Acceso

### 4.1 Entidades involucradas

- `Empleado`
- `Rol`
- Tabla de relación técnica (no entidad de negocio):
  - `EmpleadoRol` (o nombre equivalente)

### 4.2 Relaciones principales

#### 4.2.1 Empleado — Rol

| Relación                  | Cardinalidad                      | Descripción                                                                 |
|---------------------------|-----------------------------------|-----------------------------------------------------------------------------|
| Empleado (E1) — Rol (E2)  | Empleado (1) — (0..*) Rol         | Un empleado puede tener cero o varios roles.                                |
| Rol (E2) — Empleado (E1)  | Rol (1) — (0..*) Empleado         | Un rol puede asignarse a cero o muchos empleados.                           |

Modelado conceptual:

- `Empleado` y `Rol` se relacionan mediante una relación **N:M**.
- En el diseño lógico/físico, esto se implementa con una tabla intermedia `EmpleadoRol`:

  - `EmpleadoRol(idEmpleado, idRol, fechaAsignacion, activo, ...)`

### 4.3 Comentarios conceptuales

- El modelo permite:
  - Que un mismo empleado sea, por ejemplo, `CAJERO` y `OFICIAL_CUENTA`.
  - Que se agreguen nuevos roles sin cambiar la estructura de la entidad `Empleado`.
- La entidad `Rol` no almacena permisos granulares en el modelo conceptual, pero encapsula
  un “perfil” de permisos que se reflejará en la capa de aplicación/seguridad.

---

## 5. Submodelo: Clientes & Cuentas

### 5.1 Entidades involucradas

- `Cliente`
- `CuentaBancaria`

### 5.2 Relaciones principales

#### 5.2.1 Cliente — CuentaBancaria

| Relación                            | Cardinalidad                              | Descripción                                                                 |
|-------------------------------------|-------------------------------------------|-----------------------------------------------------------------------------|
| Cliente (E3) — CuentaBancaria (E4) | Cliente (1) — (0..*) CuentaBancaria       | Un cliente puede tener cero o muchas cuentas.                               |
| CuentaBancaria (E4) — Cliente (E3) | CuentaBancaria (1) — (1) Cliente          | Cada cuenta bancaria pertenece a un único cliente titular.                  |

> En el MVP se asume **un solo titular principal** por cuenta (no co-titulares).

### 5.3 Comentarios conceptuales

- La cuenta **no existe sin cliente** como concepto de negocio; la relación es fuerte:
  - Cada `CuentaBancaria` tiene una FK hacia `Cliente`.
- El modelo permite:
  - Clientes sin cuenta (por ejemplo, recién dados de alta).
  - Clientes con varias cuentas de distintos tipos (`CAJA_AHORRO`, `CUENTA_CORRIENTE`, etc.).
- En futuras versiones se podría extender a:
  - Co-titulares (relación N:M entre Cliente y Cuenta).
  - Productos específicos (cuentas especiales, paquetes, etc.).

---

## 6. Submodelo: Cuentas & Movimientos

### 6.1 Entidades involucradas

- `CuentaBancaria`
- `MovimientoCuenta`

### 6.2 Relaciones principales

#### 6.2.1 CuentaBancaria — MovimientoCuenta

| Relación                                   | Cardinalidad                              | Descripción                                                                 |
|--------------------------------------------|-------------------------------------------|-----------------------------------------------------------------------------|
| CuentaBancaria (E4) — MovimientoCuenta (E5)| CuentaBancaria (1) — (0..*) Movimiento    | Una cuenta puede tener cero o muchos movimientos registrados.               |
| MovimientoCuenta (E5) — CuentaBancaria (E4)| MovimientoCuenta (1) — (1) CuentaBancaria | Todo movimiento pertenece a una única cuenta bancaria.                      |

### 6.3 Comentarios conceptuales

- Una cuenta recién creada puede no tener movimientos (saldo 0).
- Cada movimiento representa un evento puntual que cambia el saldo.
- El conjunto de `MovimientoCuenta` asociado a una `CuentaBancaria` permite reconstruir
  el historial de la cuenta y verificar la consistencia del `saldoActual`.

---

## 7. Submodelo: Operaciones Financieras & Movimientos

### 7.1 Entidades involucradas

- `OperacionFinanciera`
- `MovimientoCuenta`
- `Empleado`

### 7.2 Relaciones principales

#### 7.2.1 OperacionFinanciera — MovimientoCuenta

| Relación                                             | Cardinalidad                                      | Descripción                                                                 |
|------------------------------------------------------|---------------------------------------------------|-----------------------------------------------------------------------------|
| OperacionFinanciera (E6) — MovimientoCuenta (E5)     | OperacionFinanciera (1) — (1..*) Movimiento       | Una operación financiera puede generar uno o varios movimientos.            |
| MovimientoCuenta (E5) — OperacionFinanciera (E6)     | MovimientoCuenta (1) — (1) OperacionFinanciera    | Cada movimiento está asociado a una única operación lógica.                 |

Ejemplos:

- **Depósito**:
  - 1 operación de tipo `DEPOSITO`.
  - 1 movimiento de tipo `CREDITO` en la cuenta destino.
- **Extracción**:
  - 1 operación de tipo `EXTRACCION`.
  - 1 movimiento de tipo `DEBITO` en la cuenta origen.
- **Transferencia interna**:
  - 1 operación de tipo `TRANSFERENCIA_INTERNA`.
  - 2 movimientos:
    - 1 `DEBITO` en la cuenta origen.
    - 1 `CREDITO` en la cuenta destino.

#### 7.2.2 Empleado — OperacionFinanciera

| Relación                                      | Cardinalidad                                  | Descripción                                                                 |
|-----------------------------------------------|-----------------------------------------------|-----------------------------------------------------------------------------|
| Empleado (E1) — OperacionFinanciera (E6)      | Empleado (1) — (0..*) OperacionFinanciera     | Un empleado puede ejecutar cero o muchas operaciones financieras.           |
| OperacionFinanciera (E6) — Empleado (E1)      | OperacionFinanciera (1) — (1) Empleado        | Cada operación financiera es ejecutada por un único empleado.               |

#### 7.2.3 Empleado — MovimientoCuenta

| Relación                                   | Cardinalidad                              | Descripción                                                                 |
|--------------------------------------------|-------------------------------------------|-----------------------------------------------------------------------------|
| Empleado (E1) — MovimientoCuenta (E5)      | Empleado (1) — (0..*) Movimiento          | Un empleado puede registrar muchos movimientos.                             |
| MovimientoCuenta (E5) — Empleado (E1)      | MovimientoCuenta (1) — (1) Empleado       | Cada movimiento tiene un empleado responsable asociado.                     |

> En el modelo conceptual del MVP se asume que el **empleado que ejecuta la operación**
> es el mismo que se registra como responsable de los movimientos generados.

---

## 8. Submodelo: Auditoría

### 8.1 Entidades involucradas

- `EventoAuditoria`
- `Empleado`
- `OperacionFinanciera` (opcional)

### 8.2 Relaciones principales

#### 8.2.1 Empleado — EventoAuditoria

| Relación                                   | Cardinalidad                                  | Descripción                                                                 |
|--------------------------------------------|-----------------------------------------------|-----------------------------------------------------------------------------|
| Empleado (E1) — EventoAuditoria (E7)       | Empleado (1) — (0..*) EventoAuditoria         | Un empleado puede originar muchos eventos de auditoría.                     |
| EventoAuditoria (E7) — Empleado (E1)       | EventoAuditoria (0..1) — (1) Empleado         | Algunos eventos pueden estar asociados a un empleado; otros, no.           |

Ejemplos:

- `LOGIN_EXITOSO` → asociado a un empleado.
- `LOGIN_FALLIDO` con usuario inexistente → podría no asociarse a ningún empleado real.

#### 8.2.2 OperacionFinanciera — EventoAuditoria

| Relación                                             | Cardinalidad                                      | Descripción                                                                 |
|------------------------------------------------------|---------------------------------------------------|-----------------------------------------------------------------------------|
| OperacionFinanciera (E6) — EventoAuditoria (E7)     | OperacionFinanciera (1) — (0..*) EventoAuditoria | Una operación puede generar cero o varios eventos de auditoría.             |
| EventoAuditoria (E7) — OperacionFinanciera (E6)     | EventoAuditoria (0..1) — (1) OperacionFinanciera | Algunos eventos estarán ligados a una operación financiera concreta.        |

### 8.3 Comentarios conceptuales

- `EventoAuditoria` actúa como entidad **transversal**, permitiendo auditar:
  - Accesos al sistema (logins).
  - Cambios de estado (bloqueo de cliente, bloqueo de empleado).
  - Operaciones financieras relevantes.
- El modelo permite asociar un evento tanto a:
  - Un `Empleado`.
  - Una `OperacionFinanciera`.
  - Otra entidad (mediante los campos `entidadTipo` y `entidadId` definidos en el paso 8).

---

## 9. Reglas de integridad de alto nivel

A nivel conceptual, el modelo debe respetar las siguientes reglas de integridad:

1. **Integridad de cliente–documento**  
   No puede existir más de un `Cliente` con la misma combinación (`tipoDocumento`, `numeroDocumento`).

2. **Integridad de cuenta–cliente**  
   - Toda `CuentaBancaria` debe estar asociada a un `Cliente` existente.  
   - No puede haber cuentas “huérfanas”.

3. **Integridad de número de cuenta**  
   - `numeroCuenta` debe ser único en el sistema.

4. **Integridad de movimientos**  
   - Todo `MovimientoCuenta` debe:
     - Estar asociado a una `CuentaBancaria` existente.
     - Estar asociado a una `OperacionFinanciera` existente (en el modelo del MVP).
     - Estar asociado a un `Empleado` existente.
   - No pueden existir movimientos sin cuenta.

5. **Integridad de saldos**  
   - Los `MovimientoCuenta` deben ser coherentes con el `saldoActual` de la `CuentaBancaria`.  
   - La suma acumulada de movimientos (aplicados en orden temporal) debe justificar el saldo.

6. **Integridad de estados**  
   - Solo cuentas en estado `ACTIVA` pueden recibir nuevos movimientos financieros.  
   - Solo clientes en estado `ACTIVO` pueden recibir nuevas cuentas.  
   - Solo empleados en estado `ACTIVO` pueden ejecutar operaciones y loguearse.

7. **Integridad de operaciones financieras**  
   - Una `OperacionFinanciera` de tipo `TRANSFERENCIA_INTERNA` debe tener:
     - `cuentaOrigenId` y `cuentaDestinoId` no nulos y distintos.
     - Al menos dos `MovimientoCuenta` asociados (DEBITO y CREDITO).

8. **Integridad de auditoría**  
   - Los `EventoAuditoria` deben registrar, como mínimo, el tipo de evento y la fecha/hora.  
   - Cuando corresponda, deben apuntar a la entidad afectada (empleado, cliente, cuenta, operación).

---

## 10. Próximos pasos

Con el modelo conceptual definido:

1. Se puede elaborar un **diagrama UML/ER más formal** (si se desea) a partir de este documento.
2. A partir de este modelo se diseña:
   - El **modelo lógico** de base de datos (tablas, PK, FK, índices).
   - Las **clases de entidad** en el backend (por ejemplo, usando JPA/Hibernate).
3. Se puede trazar la correspondencia entre:
   - Entidades/relaciones ↔ Requisitos funcionales (RF).
   - Entidades/relaciones ↔ Casos de uso y user stories.

Este documento actúa como puente entre la **visión de negocio bancario** y la
**implementación técnica** del sistema bancario educativo.
