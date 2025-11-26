# 06 — Historias de Usuario (User Stories)

## 1. Objetivo del documento

Este documento define las **Historias de Usuario (User Stories)** del sistema bancario educativo,
alineadas con:

- La **visión** del sistema (`01-vision-alcance.md`).
- Los **actores** identificados (`02-actores.md`).
- Los **casos de uso** de alto nivel (`03-casos-uso.md`).
- Los **requisitos funcionales** (`04-requisitos-funcionales.md`).

Las user stories se redactan en lenguaje cercano al usuario (empleado del banco) y sirven como
base para:

- Planificación de sprints.
- Priorización del backlog.
- Criterios de aceptación para desarrollo y testing.

---

## 2. Convenciones y nomenclatura

- Cada historia de usuario se identifica como: `US-CAT-XXX`, donde:
  - `CAT` indica una categoría funcional (AUT, EMP, CLI, CTA, OP, AUD).
  - `XXX` es un número correlativo dentro de la categoría.
- Formato de redacción:

> Como `<rol>`  
> quiero `<acción>`  
> para `<beneficio / objetivo de negocio>`.

- Cada historia incluye:
  - Descripción breve.
  - Criterios de aceptación (mínimos).
  - Referencias a **Casos de Uso (CU)** y **Requisitos Funcionales (RF)**.

---

## 3. Historias de usuario por rol

---

### 3.1 Autenticación e identidad (AUT)

#### US-AUT-001 — Iniciar sesión como empleado

**Historia**

> Como **empleado del banco**  
> quiero **iniciar sesión con mi usuario y contraseña**  
> para **acceder al sistema solo si estoy autorizado y registrar mis acciones a mi nombre**.

**Descripción breve**

Permite que cualquier empleado (cajero, oficial, administrador) se autentique en el sistema y
obtenga una sesión asociada a sus roles y permisos.

**Criterios de aceptación**

- Dado un empleado **ACTIVO** con usuario y contraseña válidos:
  - Cuando ingresa sus credenciales correctas,
  - Entonces el sistema lo autentica y lo redirige a la interfaz correspondiente a sus permisos.
- Dado un usuario o contraseña incorrectos:
  - Cuando intenta iniciar sesión,
  - Entonces el sistema rechaza el acceso y registra el intento fallido.
- Dado un empleado en estado **BLOQUEADO**:
  - Cuando intenta iniciar sesión,
  - Entonces el sistema debe impedir el acceso, aunque las credenciales sean correctas.

**Referencias**

- CU: `CU-AUT-001`
- RF: `RF-001`, `RF-002`, `RF-003`, `RF-041`

---

### 3.2 Administrador del Sistema (EMP)

#### US-EMP-001 — Dar de alta empleados

**Historia**

> Como **administrador del sistema**  
> quiero **dar de alta nuevos empleados con usuario y rol asignado**  
> para **permitir que puedan operar en el sistema de acuerdo a sus funciones**.

**Descripción breve**

Permite registrar nuevos empleados internos, asignarles usuario, estado inicial y uno o más roles.

**Criterios de aceptación**

- Debe ser posible registrar un nuevo empleado con:
  - Nombre.
  - Usuario único.
  - Estado inicial (por defecto ACTIVO).
  - Uno o más roles (ej.: CAJERO, OFICIAL_CUENTA, ADMIN).
- Si el nombre de usuario ya existe:
  - El sistema debe rechazar el alta e informar el conflicto.
- Tras el alta, el nuevo empleado debe poder iniciar sesión (si está ACTIVO).

**Referencias**

- CU: `CU-EMP-001`
- RF: `RF-004`, `RF-005`

---

#### US-EMP-002 — Asignar o modificar roles de empleados

**Historia**

> Como **administrador del sistema**  
> quiero **asignar o modificar los roles de un empleado**  
> para **controlar qué funcionalidades del sistema puede utilizar**.

**Descripción breve**

Permite ajustar los permisos de un empleado según cambios en su puesto o responsabilidades.

**Criterios de aceptación**

- Debe ser posible:
  - Agregar un rol a un empleado.
  - Quitar un rol de un empleado.
- Los cambios de rol deben impactar en los permisos al próximo inicio de sesión.
- Un empleado sin roles no debe poder acceder a funcionalidades restringidas.

**Referencias**

- CU: `CU-EMP-002`
- RF: `RF-005`, `RF-002`

---

#### US-EMP-003 — Bloquear empleados

**Historia**

> Como **administrador del sistema**  
> quiero **bloquear el acceso de un empleado**  
> para **impedir que siga operando cuando ya no debe usar el sistema**.

**Descripción breve**

Permite cambiar el estado de un empleado a BLOQUEADO, evitando futuros accesos.

**Criterios de aceptación**

- Dado un empleado ACTIVO:
  - Cuando el administrador cambia su estado a BLOQUEADO,
  - Entonces el empleado no debe poder iniciar sesión nuevamente.
- El bloqueo debe quedar registrado para auditoría (quién bloqueó, a quién y cuándo).

**Referencias**

- CU: `CU-EMP-003`
- RF: `RF-006`, `RF-002`, `RF-041`

---

### 3.3 Oficial de Cuentas (CLI / CTA)

#### US-CLI-001 — Dar de alta clientes

**Historia**

> Como **oficial de cuentas**  
> quiero **dar de alta nuevos clientes con sus datos básicos y documento único**  
> para **que puedan operar y ser titulares de cuentas bancarias**.

**Descripción breve**

Permite registrar clientes, garantizando que el documento de identidad no esté duplicado.

**Criterios de aceptación**

- Debe ser obligatorio ingresar:
  - Nombre/apellido.
  - Documento de identidad.
  - Algún dato de contacto básico.
- Si ya existe un cliente con el mismo documento:
  - El sistema debe rechazar el alta e informar la duplicidad.
- El cliente nuevo debe quedar en estado ACTIVO y listo para asociar cuentas.

**Referencias**

- CU: `CU-CLI-001`
- RF: `RF-010`, `RF-013`

---

#### US-CLI-002 — Actualizar datos de clientes

**Historia**

> Como **oficial de cuentas**  
> quiero **actualizar los datos de un cliente existente**  
> para **mantener su información al día y evitar errores operativos**.

**Descripción breve**

Permite modificar datos no identificatorios (domicilio, contacto, etc.) de clientes existentes.

**Criterios de aceptación**

- Debe ser posible actualizar datos de contacto y otros campos editables.
- El documento de identidad no debe poder duplicarse con el de otro cliente.
- El sistema debe mostrar al menos:
  - Última fecha de actualización.
  - Quién realizó el cambio (empleado).

**Referencias**

- CU: `CU-CLI-002`
- RF: `RF-011`, `RF-013`, `RF-040`

---

#### US-CLI-003 — Bloquear clientes

**Historia**

> Como **oficial de cuentas**  
> quiero **bloquear a un cliente**  
> para **impedir que se le abran nuevas cuentas u operaciones si presenta riesgo o incumplimientos**.

**Descripción breve**

Permite cambiar el estado del cliente a BLOQUEADO, afectando su capacidad de operar.

**Criterios de aceptación**

- Dado un cliente ACTIVO:
  - Cuando es bloqueado,
  - Entonces el sistema debe impedir:
    - Apertura de nuevas cuentas a su nombre.
    - Nuevas operaciones sobre sus cuentas, según reglas definidas.
- El bloqueo debe quedar registrado con:
  - Empleado que ejecutó la acción.
  - Fecha/hora.
  - Motivo (si se implementa).

**Referencias**

- CU: `CU-CLI-003`
- RF: `RF-012`, `RF-024`, `RF-040`

---

#### US-CTA-001 — Abrir cuenta bancaria para cliente

**Historia**

> Como **oficial de cuentas**  
> quiero **abrir una nueva cuenta bancaria para un cliente existente**  
> para **habilitarlo a realizar operaciones financieras en el banco**.

**Descripción breve**

Permite crear cuentas de tipo CAJA_AHORRO o CUENTA_CORRIENTE para clientes válidos.

**Criterios de aceptación**

- Solo se puede abrir una cuenta para:
  - Clientes existentes.
  - Clientes en estado ACTIVO.
- Debe seleccionarse un tipo de cuenta válido de un catálogo (ej.: CAJA_AHORRO).
- El sistema debe:
  - Generar un número de cuenta único.
  - Inicializar saldo en 0.
  - Establecer estado ACTIVA.
- Debe quedar registrada la fecha de apertura y el empleado que creó la cuenta.

**Referencias**

- CU: `CU-CTA-001`
- RF: `RF-020`, `RF-021`, `RF-024`

---

### 3.4 Cajero y operaciones sobre cuentas (CTA / OP)

#### US-CTA-002 — Consultar saldo de cuenta

**Historia**

> Como **cajero**  
> quiero **consultar el saldo actual de una cuenta bancaria**  
> para **informar al cliente y verificar si puede realizar una operación**.

**Descripción breve**

Permite a empleados autorizados ver el saldo actual de una cuenta.

**Criterios de aceptación**

- Solo empleados autenticados deben poder consultar saldos.
- La consulta debe mostrar:
  - Número de cuenta.
  - Titular (cliente).
  - Saldo actual.
  - Estado de la cuenta (ACTIVA, BLOQUEADA, CERRADA).
- Si la cuenta no existe o está en un estado inválido para operar, debe indicarse.

**Referencias**

- CU: `CU-CTA-002`
- RF: `RF-022`, `RF-024`

---

#### US-CTA-003 — Consultar movimientos de cuenta

**Historia**

> Como **cajero u oficial de cuentas**  
> quiero **consultar el historial de movimientos de una cuenta**  
> para **analizar su comportamiento y responder consultas del cliente**.

**Descripción breve**

Permite ver los movimientos (débitos/créditos) que han afectado una cuenta.

**Criterios de aceptación**

- La lista debe mostrar al menos:
  - Fecha/hora.
  - Tipo (DEBITO/CRÉDITO).
  - Monto.
  - Saldo posterior.
  - Descripción/concepto.
- Debe ser posible filtrar por rango de fechas (en una versión posterior, opcionalmente).
- Debe respetar permisos: solo empleados autorizados pueden acceder a esta información.

**Referencias**

- CU: `CU-CTA-003`
- RF: `RF-023`, `RF-040`, `RF-042`

---

#### US-OP-001 — Registrar depósito en cuenta

**Historia**

> Como **cajero**  
> quiero **registrar depósitos en las cuentas de los clientes**  
> para **acreditar fondos y reflejar correctamente el saldo disponible**.

**Descripción breve**

Permite aumentar el saldo de una cuenta mediante un depósito y registrar el movimiento asociado.

**Criterios de aceptación**

- La cuenta debe existir y estar ACTIVA.
- El monto del depósito debe ser mayor que cero.
- Al confirmar el depósito, el sistema debe:
  - Actualizar el saldo.
  - Registrar un movimiento de tipo CRÉDITO.
  - Asociar la operación al empleado que la ejecutó.
- El sistema debe mostrar el saldo actualizado y algún identificador de operación.

**Referencias**

- CU: `CU-OP-001`
- RF: `RF-030`, `RF-033`, `RF-040`

---

#### US-OP-002 — Registrar extracción de cuenta

**Historia**

> Como **cajero**  
> quiero **registrar extracciones de fondos de las cuentas de los clientes**  
> para **permitir retiros controlados que respeten el saldo disponible**.

**Descripción breve**

Permite disminuir el saldo de una cuenta, validando saldo suficiente.

**Criterios de aceptación**

- La cuenta debe existir y estar ACTIVA.
- El monto de extracción debe ser mayor que cero.
- El sistema debe verificar que el saldo es suficiente antes de debitar.
- Si hay saldo suficiente:
  - Disminuye el saldo.
  - Crea un movimiento de tipo DEBITO.
- Si no hay saldo suficiente:
  - Rechaza la operación.
  - No cambia saldo ni registra movimiento financiero.

**Referencias**

- CU: `CU-OP-002`
- RF: `RF-031`, `RF-033`, `RF-040`

---

#### US-OP-003 — Registrar transferencia interna entre cuentas

**Historia**

> Como **cajero u oficial de cuentas**  
> quiero **registrar transferencias internas entre cuentas del banco**  
> para **mover fondos de manera segura y consistente entre cuentas de clientes**.

**Descripción breve**

Permite mover fondos de una cuenta origen a una cuenta destino del mismo banco.

**Criterios de aceptación**

- Tanto la cuenta origen como la destino deben:
  - Existir.
  - Estar en estado ACTIVA.
- Las cuentas origen y destino deben ser diferentes.
- La cuenta origen debe tener saldo suficiente.
- La operación debe:
  - Disminuir el saldo en la cuenta origen (DEBITO).
  - Aumentar el saldo en la cuenta destino (CRÉDITO).
  - Crear dos movimientos vinculados con un identificador común de operación.
- Si alguna validación falla:
  - La transferencia debe ser rechazada sin afectar saldos.

**Referencias**

- CU: `CU-OP-003`
- RF: `RF-032`, `RF-033`, `RF-040`

---

### 3.5 Auditoría (AUD) — Presente y futuro

#### US-AUD-001 — Consultar trazabilidad de operaciones

**Historia**

> Como **administrador del sistema**  
> quiero **consultar quién realizó una operación sobre una cuenta y cuándo**  
> para **poder investigar incidentes, errores o sospechas de fraude**.

**Descripción breve**

Permite recuperar, para una operación (o conjunto de operaciones), la información necesaria
para entender qué pasó.

**Criterios de aceptación**

- Debe ser posible buscar operaciones por:
  - Cuenta.
  - Rango de fechas.
  - Tipo de operación (depósito, extracción, transferencia).
- Para cada operación debe mostrarse:
  - Empleado que la ejecutó.
  - Fecha/hora.
  - Tipo y monto.
  - Cuentas involucradas (si aplica).
- El acceso a esta funcionalidad debe estar restringido a roles con permisos de auditoría
  (por ahora, al menos ADMIN).

**Referencias**

- CU: `CU-AUD-001`
- RF: `RF-040`, `RF-041`, `RF-042`, `RF-AUD-001`, `RF-AUD-003` (según numeración de RNF)

---

## 4. Tabla resumen de historias de usuario

| ID           | Rol principal            | Nombre breve                                     | CU relacionados                 |
|--------------|--------------------------|--------------------------------------------------|---------------------------------|
| US-AUT-001   | Empleado                 | Iniciar sesión                                   | CU-AUT-001                      |
| US-EMP-001   | Administrador            | Dar de alta empleados                            | CU-EMP-001                      |
| US-EMP-002   | Administrador            | Asignar/modificar roles                          | CU-EMP-002                      |
| US-EMP-003   | Administrador            | Bloquear empleados                               | CU-EMP-003                      |
| US-CLI-001   | Oficial de Cuentas       | Dar de alta clientes                             | CU-CLI-001                      |
| US-CLI-002   | Oficial de Cuentas       | Actualizar datos de clientes                     | CU-CLI-002                      |
| US-CLI-003   | Oficial de Cuentas       | Bloquear clientes                                | CU-CLI-003                      |
| US-CTA-001   | Oficial de Cuentas       | Abrir cuenta bancaria para cliente               | CU-CTA-001                      |
| US-CTA-002   | Cajero                   | Consultar saldo de cuenta                        | CU-CTA-002                      |
| US-CTA-003   | Cajero / Oficial         | Consultar movimientos de cuenta                  | CU-CTA-003                      |
| US-OP-001    | Cajero                   | Registrar depósito en cuenta                     | CU-OP-001                       |
| US-OP-002    | Cajero                   | Registrar extracción de cuenta                   | CU-OP-002                       |
| US-OP-003    | Cajero / Oficial         | Registrar transferencia interna entre cuentas    | CU-OP-003                       |
| US-AUD-001   | Administrador / Auditor  | Consultar trazabilidad de operaciones            | CU-AUD-001                      |

---

Estas historias de usuario forman el **backlog inicial** del sistema bancario educativo.
A partir de ellas podés:

- Crear issues en GitHub por historia.
- Dividir cada historia en tareas técnicas (entidades, servicios, endpoints, tests).
- Priorizar el desarrollo por módulos (Identidad & Acceso → Clientes → Cuentas → Operaciones → Auditoría).
