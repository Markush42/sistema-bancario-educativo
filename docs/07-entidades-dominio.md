# 07 — Entidades del Dominio del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define las **entidades de dominio** del sistema bancario educativo.

Una entidad de dominio representa un **concepto de negocio relevante y persistente** en el banco
(empleados, clientes, cuentas, movimientos, operaciones, etc.), y sirve como base para:

- El **modelo conceptual / UML** (`09-modelo-conceptual.md`).
- El **detalle de atributos y tipos** (`08-entidades-detalle.md`).
- El diseño de clases de dominio y tablas de base de datos.

El alcance se centra en el **MVP** definido en:

- `01-vision-alcance.md`
- `04-requisitos-funcionales.md`
- `06-user-stories.md`

---

## 2. Criterios para considerar algo una entidad de dominio

Se considera entidad de dominio a todo concepto que:

1. Representa algo **importante para el negocio bancario** (no solo un detalle técnico).
2. Tiene **identidad propia** (un identificador que lo diferencia de otros).
3. Puede **persistir en el tiempo** (no es efímero ni solo de una operación puntual).
4. Tiene **reglas de negocio asociadas** (estados, restricciones, comportamientos).

Bajo estos criterios, se seleccionan las entidades descritas en la siguiente sección.

---

## 3. Lista de entidades de dominio del MVP

### 3.1 Tabla resumen

| ID  | Entidad              | Tipo / Naturaleza  | Descripción breve                                                                                 |
|-----|----------------------|--------------------|----------------------------------------------------------------------------------------------------|
| E1  | Empleado             | Persona interna    | Representa a un usuario interno del banco que opera el sistema (cajero, oficial, administrador).  |
| E2  | Rol                  | Configuración      | Define un rol o perfil de permisos asignable a empleados (ADMIN, CAJERO, OFICIAL_CUENTA, etc.).   |
| E3  | Cliente              | Persona externa    | Representa a la persona (física o jurídica) titular de cuentas y destinataria de los servicios.   |
| E4  | CuentaBancaria       | Activo financiero  | Representa una cuenta bancaria asociada a un cliente, con saldo, tipo y estado.                   |
| E5  | MovimientoCuenta     | Registro financiero| Representa un movimiento de débito/crédito que afecta el saldo de una cuenta bancaria.            |
| E6  | OperacionFinanciera  | Agrupador lógico   | Representa una operación financiera lógica (depósito, extracción, transferencia) que genera uno o más movimientos. |
| E7  | EventoAuditoria      | Registro de auditoría | Representa un evento relevante para auditoría (operación, acceso, bloqueo, etc.).              |

> Nota: algunas entidades (por ejemplo, `OperacionFinanciera` y `EventoAuditoria`) pueden
> implementarse como tablas propias o como parte de otras estructuras. Aquí se definen como
> entidades de dominio para clarificar el modelo de negocio.

---

## 4. Descripción de cada entidad

---

### 4.1 Empleado (E1)

**Rol en el dominio**

Representa a cualquier usuario interno que interactúa con el sistema bancario:

- Cajero.
- Oficial de Cuentas.
- Administrador del Sistema.

Es una entidad central para la **trazabilidad**: casi toda operación relevante debe poder
atribuirse a un empleado.

**Responsabilidades principales**

- Autenticarse en el sistema (usuario/contraseña).
- Ejecutar operaciones según su rol:
  - Cajero: operaciones sobre cuentas (depósitos, extracciones, transferencias).
  - Oficial de Cuentas: alta de clientes, apertura de cuentas, bloqueo de clientes.
  - Administrador: gestión de empleados y roles, tareas administrativas.
- Ser referencia en registros de auditoría y movimientos (quién hizo qué).

**Relaciones clave**

| Relación                     | Tipo        | Descripción                                                               |
|-----------------------------|------------|---------------------------------------------------------------------------|
| Empleado–Rol                | N:M        | Un empleado puede tener uno o varios roles; un rol puede tener varios empleados. |
| Empleado–MovimientoCuenta   | 1:N        | Un empleado puede ser responsable de muchos movimientos registrados.      |
| Empleado–EventoAuditoria    | 1:N        | Los eventos de auditoría registran el empleado que los originó (cuando aplica). |

**Comentarios de diseño**

- El `Empleado` debe tener concepto de **estado** (ACTIVO, BLOQUEADO).
- El usuario de autenticación pertenece a esta entidad (no se modela usuario aparte en el MVP).

---

### 4.2 Rol (E2)

**Rol en el dominio**

Define un **perfil de permisos** que será asignado a uno o más empleados. Permite implementar
un esquema de **control de acceso basado en roles (RBAC)**.

Ejemplos de roles:

- `ADMIN`
- `CAJERO`
- `OFICIAL_CUENTA`

**Responsabilidades principales**

- Encapsular el **conjunto de permisos** asociados a un tipo de usuario interno.
- Permitir al Administrador cambiar las capacidades de un empleado mediante asignación de roles.

**Relaciones clave**

| Relación           | Tipo | Descripción                                                 |
|--------------------|------|-------------------------------------------------------------|
| Rol–Empleado       | N:M  | Un rol puede asignarse a muchos empleados y viceversa.      |

**Comentarios de diseño**

- A nivel de dominio, no se detallan permisos individuales; se asume que los roles condicionan
  el acceso a funcionalidades (casos de uso).

---

### 4.3 Cliente (E3)

**Rol en el dominio**

Representa a la persona (física o jurídica) que mantiene una **relación comercial** con el banco
y puede ser titular de una o más cuentas bancarias.

Aunque en el MVP el cliente **no interactúa directamente** con el sistema, es el foco del negocio.

**Responsabilidades principales**

- Ser titular de cuentas bancarias.
- Ser referencia para todas las operaciones realizadas en sus cuentas.
- Mantener información de identificación y contacto válida.

**Relaciones clave**

| Relación              | Tipo | Descripción                                               |
|-----------------------|------|-----------------------------------------------------------|
| Cliente–CuentaBancaria| 1:N  | Un cliente puede tener una o más cuentas bancarias.       |

**Comentarios de diseño**

- El documento de identidad del cliente debe ser **único**.
- Debe tener un **estado** (ACTIVO, BLOQUEADO) que afecte la posibilidad de operar.

---

### 4.4 CuentaBancaria (E4)

**Rol en el dominio**

Representa una **cuenta bancaria** asociada a un cliente y mantenida por el banco, con:

- Tipo de cuenta (`CAJA_AHORRO`, `CUENTA_CORRIENTE`, etc.).
- Saldo actual.
- Estado (ACTIVA, BLOQUEADA, CERRADA).

Es el núcleo del modelo financiero del sistema.

**Responsabilidades principales**

- Mantener un **saldo consistente** a partir de los movimientos aplicados.
- Permitir ejecutar operaciones de depósito, extracción y transferencia interna (si está ACTIVA).
- Representar la situación financiera del cliente frente al banco (a nivel de cuenta).

**Relaciones clave**

| Relación                      | Tipo | Descripción                                                        |
|-------------------------------|------|--------------------------------------------------------------------|
| CuentaBancaria–Cliente        | N:1  | Cada cuenta pertenece a un único cliente titular.                  |
| CuentaBancaria–MovimientoCuenta| 1:N | Una cuenta tiene muchos movimientos de débito/crédito asociados.   |

**Comentarios de diseño**

- El número de cuenta debe ser **único** en el sistema.
- El saldo puede derivarse de la suma de movimientos, pero en la práctica se almacena para eficiencia.

---

### 4.5 MovimientoCuenta (E5)

**Rol en el dominio**

Representa un **movimiento financiero** que afecta el saldo de una cuenta bancaria:

- DEBITO (disminuye saldo).
- CRÉDITO (aumenta saldo).

Cada operación sobre cuentas (depósito, extracción, transferencia) genera uno o más movimientos.

**Responsabilidades principales**

- Registrar el efecto concreto de una operación sobre el saldo de una cuenta.
- Mantener trazabilidad del cambio de saldo (monto, saldo posterior, fecha, empleado).
- Servir como base para **consultas de movimientos** y auditoría.

**Relaciones clave**

| Relación                         | Tipo | Descripción                                                     |
|----------------------------------|------|-----------------------------------------------------------------|
| MovimientoCuenta–CuentaBancaria | N:1  | Cada movimiento pertenece a una única cuenta bancaria.          |
| MovimientoCuenta–Empleado       | N:1  | Un empleado es responsable de registrar el movimiento.          |
| MovimientoCuenta–OperacionFinanciera | N:1 (opcional) | Varios movimientos pueden pertenecer a una misma operación lógica. |

**Comentarios de diseño**

- Debe almacenar al menos:
  - Fecha/hora.
  - Tipo (DEBITO/CRÉDITO).
  - Monto.
  - Saldo posterior.
- Es el objeto principal para reconstruir el historial de una cuenta.

---

### 4.6 OperacionFinanciera (E6)

**Rol en el dominio**

Representa una **operación financiera lógica** iniciada por un empleado y que puede impactar
a una o varias cuentas.

Ejemplos:

- Depósito en una cuenta.
- Extracción de una cuenta.
- Transferencia interna (una operación lógica que genera dos movimientos: débito y crédito).

**Responsabilidades principales**

- Agrupar uno o más movimientos de cuenta bajo una misma operación.
- Proveer un identificador común para trazabilidad y auditoría.
- Representar el contexto de la operación (tipo, origen, destino, empleado ejecutor).

**Relaciones clave**

| Relación                         | Tipo | Descripción                                                  |
|----------------------------------|------|--------------------------------------------------------------|
| OperacionFinanciera–MovimientoCuenta | 1:N | Una operación puede generar uno o varios movimientos.    |
| OperacionFinanciera–Empleado     | N:1  | Un empleado es responsable de ejecutar la operación.        |

**Comentarios de diseño**

- En el caso de una transferencia interna:
  - La `OperacionFinanciera` representa la transferencia.
  - Los `MovimientoCuenta` representan el débito en origen y el crédito en destino.
- Puede modelarse como entidad explícita o inferirse por un identificador común en movimientos.

---

### 4.7 EventoAuditoria (E7)

**Rol en el dominio**

Representa un **evento relevante para auditoría**, no necesariamente financiero, por ejemplo:

- Intento de login (exitoso o fallido).
- Bloqueo de empleado.
- Bloqueo de cliente.
- Ejecución de una operación financiera (también vinculada a `OperacionFinanciera`).

**Responsabilidades principales**

- Registrar hechos importantes para análisis posterior (seguridad, cumplimiento, debugging).
- Mantener información de:
  - Tipo de evento.
  - Fecha/hora.
  - Empleado involucrado (cuando aplica).
  - Entidad afectada (cliente, cuenta, empleado, etc.).

**Relaciones clave**

| Relación                       | Tipo | Descripción                                                         |
|--------------------------------|------|---------------------------------------------------------------------|
| EventoAuditoria–Empleado       | N:1  | Un empleado puede originar muchos eventos de auditoría.             |
| EventoAuditoria–OperacionFinanciera | N:1 (opcional) | Una operación financiera puede generar uno o más eventos asociados. |

**Comentarios de diseño**

- Es una entidad transversal a varios módulos:
  - Identidad & Acceso (logins, bloqueos).
  - Clientes (bloqueos).
  - Operaciones (registro de operaciones relevantes).
- Permite implementar los RNF de auditoría y logging definidos en `05-requisitos-no-funcionales.md`.

---

## 5. Resumen y próximos pasos

Las entidades definidas en este documento representan el **núcleo del dominio bancario** del MVP:

- Personas internas (`Empleado`) y externas (`Cliente`).
- Estructuras financieras (`CuentaBancaria`, `MovimientoCuenta`, `OperacionFinanciera`).
- Soporte de control y seguridad (`Rol`, `EventoAuditoria`).

En los siguientes pasos se deberá:

1. Detallar los **atributos y tipos de datos** de cada entidad en  
   `08-entidades-detalle.md`.
2. Representar estas entidades y relaciones en un **modelo conceptual / UML** en  
   `09-modelo-conceptual.md`.
3. Usar este modelo como base para:
   - El diseño de tablas de base de datos.
   - Las clases de dominio en el backend.
