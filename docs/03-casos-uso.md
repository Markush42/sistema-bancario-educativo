# 03 — Casos de Uso de Alto Nivel

## 1. Objetivo del documento

Este documento describe los **casos de uso de alto nivel** del sistema bancario educativo,
enfocados en:

- La operatoria de **empleados** (cajeros, oficiales, administradores).
- La gestión de **clientes** y **cuentas bancarias**.
- Las **operaciones básicas** sobre cuentas: depósitos, extracciones, transferencias internas.
- La **auditoría mínima** de lo que sucede en el sistema.

Los casos de uso aquí definidos servirán como base para los requisitos funcionales
(`04-requisitos-funcionales.md`) y para el diseño de servicios de aplicación.

---

## 2. Lista de casos de uso — MVP Versión 1

### 2.1 Tabla resumen de casos de uso

| ID           | Nombre                                      | Actor principal                  | Objetivo de negocio                                                  | Módulo                   |
|--------------|---------------------------------------------|----------------------------------|---------------------------------------------------------------------|--------------------------|
| CU-AUT-001   | Iniciar sesión de empleado                  | Empleado (cualquier rol)        | Permitir acceso seguro al sistema según rol asignado.               | Identidad & Acceso       |
| CU-EMP-001   | Dar de alta empleado                        | Administrador del Sistema       | Registrar nuevos empleados que podrán operar en el sistema.         | Identidad & Acceso       |
| CU-EMP-002   | Asignar/actualizar rol de empleado          | Administrador del Sistema       | Definir el conjunto de permisos de un empleado.                     | Identidad & Acceso       |
| CU-EMP-003   | Bloquear empleado                           | Administrador del Sistema       | Impedir el acceso al sistema a empleados inactivos/riesgosos.       | Identidad & Acceso       |
| CU-CLI-001   | Dar de alta cliente                         | Oficial de Cuentas              | Registrar un nuevo cliente del banco.                               | Clientes                 |
| CU-CLI-002   | Actualizar datos de cliente                 | Oficial de Cuentas              | Mantener actualizada la información del cliente.                    | Clientes                 |
| CU-CLI-003   | Bloquear cliente                            | Oficial de Cuentas              | Impedir nuevas operaciones para clientes en situación de riesgo.    | Clientes                 |
| CU-CTA-001   | Abrir cuenta bancaria                       | Oficial de Cuentas              | Crear una nueva cuenta para un cliente existente.                   | Cuentas Bancarias        |
| CU-CTA-002   | Consultar saldo de cuenta                   | Cajero / Oficial de Cuentas     | Obtener el saldo actualizado de una cuenta.                         | Cuentas Bancarias        |
| CU-CTA-003   | Consultar movimientos de cuenta             | Cajero / Oficial de Cuentas     | Revisar el historial de movimientos de una cuenta.                  | Cuentas Bancarias        |
| CU-OP-001    | Registrar depósito en cuenta                | Cajero                          | Acreditar fondos en una cuenta bancaria.                            | Operaciones / Transacciones |
| CU-OP-002    | Registrar extracción de cuenta              | Cajero                          | Debitar fondos de una cuenta bancaria.                              | Operaciones / Transacciones |
| CU-OP-003    | Registrar transferencia interna entre cuentas| Cajero / Oficial de Cuentas    | Mover fondos entre cuentas del mismo banco.                         | Operaciones / Transacciones |
| CU-AUD-001   | Consultar trazabilidad de operación         | Administrador / Auditor Interno | Revisar quién ejecutó una operación, sobre qué cuenta y cuándo.     | Auditoría                |

> Nota: algunos casos de uso (como CU-AUD-001) pueden tener implementación parcial
> en el MVP, pero se documentan desde ahora para mantener la visión de trazabilidad.

---

## 3. Especificación detallada de casos de uso críticos

A continuación se detallan algunos casos de uso clave para el funcionamiento del sistema.

---

### 3.1 CU-AUT-001 — Iniciar sesión de empleado

**ID**: CU-AUT-001  
**Actor principal**: Empleado (cualquier rol)  
**Módulo**: Identidad & Acceso

#### Objetivo

Permitir que un empleado acceda al sistema de forma autenticada y con el **rol adecuado**, para
que solo vea y ejecute operaciones acordes a sus responsabilidades.

#### Precondiciones

- El empleado debe existir en el sistema.
- El empleado debe estar en estado **ACTIVO**.
- Debe tener asignado al menos un rol.

#### Postcondiciones

- El empleado queda autenticado en una sesión válida.
- El sistema conoce el conjunto de permisos asociados al usuario para esta sesión.
- En caso de fallo, se registra el intento fallido de acceso (para auditoría básica).

#### Flujo básico

1. El empleado ingresa su usuario y contraseña.
2. El sistema valida las credenciales.
3. El sistema verifica que el empleado esté en estado ACTIVO.
4. El sistema recupera los roles/permisos asociados al empleado.
5. El sistema crea una sesión autenticada y permite el acceso a la interfaz correspondiente.
6. Se registra el evento de login exitoso (empleado, fecha/hora).

#### Flujo alternativo — Credenciales inválidas

- Si las credenciales no son válidas, el sistema:
  - Rechaza el acceso.
  - Registra el intento fallido con fecha/hora.
  - Opcionalmente incrementa un contador de intentos fallidos.

---

### 3.2 CU-CLI-001 — Dar de alta cliente

**ID**: CU-CLI-001  
**Actor principal**: Oficial de Cuentas  
**Módulo**: Clientes

#### Objetivo

Registrar un nuevo cliente en el sistema para que pueda ser titular de una o más cuentas bancarias.

#### Precondiciones

- El oficial de cuentas debe estar autenticado y con permisos válidos.
- Los datos mínimos del cliente deben estar disponibles (ej. nombre, documento).

#### Postcondiciones

- Se crea un registro de cliente con estado inicial **ACTIVO**.
- El documento del cliente (DNI / CUIT / equivalente) queda registrado como único.
- El sistema permite asociar cuentas a este cliente en futuros casos de uso.

#### Flujo básico

1. El oficial de cuentas selecciona la opción “Alta de cliente”.
2. El sistema solicita datos obligatorios (nombre, apellido, documento, datos de contacto, etc.).
3. El oficial carga la información del cliente.
4. El sistema valida:
   - Formato del documento.
   - Ausencia de cliente previo con el mismo documento.
5. Si las validaciones son correctas:
   - El sistema crea el cliente con estado ACTIVO.
   - Se asigna un identificador interno único.
6. Se confirma al oficial que el cliente fue creado correctamente.

#### Flujo alternativo — Documento ya registrado

- Si existe un cliente con el mismo documento:
  - El sistema rechaza el alta.
  - Muestra un mensaje de error indicando la duplicidad.
  - Opcionalmente ofrece ver el cliente existente.

---

### 3.3 CU-CTA-001 — Abrir cuenta bancaria

**ID**: CU-CTA-001  
**Actor principal**: Oficial de Cuentas  
**Módulo**: Cuentas Bancarias

#### Objetivo

Crear una nueva cuenta bancaria para un cliente existente, definiendo tipo de cuenta y moneda
según el catálogo permitido.

#### Precondiciones

- El oficial de cuentas está autenticado.
- El cliente ya existe en el sistema y está en estado **ACTIVO**.
- Se han definido los tipos de cuenta permitidos (por ejemplo, `CAJA_AHORRO`, `CUENTA_CORRIENTE`).

#### Postcondiciones

- Se crea una cuenta bancaria asociada al cliente, con:
  - Número de cuenta único.
  - Tipo de cuenta definido.
  - Saldo inicial en cero.
  - Estado inicial **ACTIVA**.
- La cuenta queda disponible para operaciones (depósitos, extracciones, transferencias).

#### Flujo básico

1. El oficial de cuentas busca y selecciona un cliente existente.
2. El oficial elige la opción “Abrir cuenta bancaria”.
3. El sistema solicita:
   - Tipo de cuenta.
   - Moneda (si aplica).
4. El oficial selecciona el tipo de cuenta (por ejemplo: CAJA_AHORRO).
5. El sistema genera internamente un número de cuenta único.
6. El sistema crea la cuenta con saldo inicial 0 y estado ACTIVA.
7. El sistema confirma la creación de la cuenta, mostrando los datos principales.

#### Flujo alternativo — Cliente bloqueado

- Si el cliente está en estado BLOQUEADO:
  - El sistema impide la apertura de cuentas.
  - Muestra un mensaje informando que el cliente no admite nuevas cuentas.

---

### 3.4 CU-OP-001 — Registrar depósito en cuenta

**ID**: CU-OP-001  
**Actor principal**: Cajero  
**Módulo**: Operaciones / Transacciones

#### Objetivo

Acreditar fondos en una cuenta bancaria, aumentando su saldo y registrando la operación con
trazabilidad completa.

#### Precondiciones

- El cajero está autenticado.
- La cuenta existe y está en estado **ACTIVA**.
- El monto del depósito es mayor que cero.

#### Postcondiciones

- El saldo de la cuenta aumenta en el monto del depósito.
- Se registra un movimiento de tipo **CRÉDITO** con:
  - Monto.
  - Fecha/hora.
  - Saldo posterior a la operación.
  - Referencia al empleado que ejecutó la operación.
- La operación queda disponible para consulta en el historial de movimientos y en auditoría.

#### Flujo básico

1. El cajero ingresa:
   - Identificación de cuenta.
   - Monto a depositar.
   - Información adicional (ej. referencia, concepto).
2. El sistema valida:
   - Existencia de la cuenta.
   - Estado ACTIVA de la cuenta.
   - Monto > 0.
3. El sistema calcula el nuevo saldo (saldo actual + monto).
4. El sistema:
   - Actualiza el saldo de la cuenta.
   - Crea un registro de movimiento de tipo CRÉDITO.
5. El sistema muestra al cajero:
   - Confirmación del depósito.
   - Saldo actualizado.

#### Flujo alternativo — Cuenta no activa

- Si la cuenta está BLOQUEADA o CERRADA:
  - El sistema rechaza el depósito.
  - Muestra mensaje indicando que la cuenta no admite operaciones.

---

### 3.5 CU-OP-003 — Registrar transferencia interna entre cuentas

**ID**: CU-OP-003  
**Actor principal**: Cajero u Oficial de Cuentas  
**Módulo**: Operaciones / Transacciones

#### Objetivo

Mover fondos de una cuenta origen a una cuenta destino dentro del mismo banco, garantizando
consistencia en saldos y movimientos.

#### Precondiciones

- El empleado (cajero u oficial) está autenticado.
- La cuenta origen existe, está ACTIVA y pertenece a un cliente válido.
- La cuenta destino existe, está ACTIVA.
- La cuenta origen tiene saldo suficiente para cubrir el monto a transferir.
- El monto de la transferencia es mayor que cero.

#### Postcondiciones

- El saldo de la cuenta origen disminuye en el monto transferido.
- El saldo de la cuenta destino aumenta en el mismo monto.
- Se registran dos movimientos:
  - **DEBITO** en la cuenta origen.
  - **CRÉDITO** en la cuenta destino.
- Ambos movimientos comparten un identificador de operación/transferencia para trazabilidad.
- La operación queda disponible para consulta y auditoría.

#### Flujo básico

1. El empleado selecciona la opción “Transferencia interna”.
2. El sistema solicita:
   - Cuenta origen.
   - Cuenta destino.
   - Monto.
   - Concepto / referencia.
3. El sistema valida:
   - Existencia y estado ACTIVO de ambas cuentas.
   - Que la cuenta origen tenga saldo suficiente.
   - Que cuenta origen y destino no sean la misma.
4. Si las validaciones son correctas:
   - El sistema debita el monto de la cuenta origen.
   - El sistema acredita el monto en la cuenta destino.
   - Crea los dos movimientos correspondientes con un identificador común de operación.
5. El sistema muestra la confirmación de la transferencia y los saldos resultantes.

#### Flujos alternativos

- **Saldo insuficiente en cuenta origen**:
  - El sistema rechaza la operación.
  - Informa que no hay fondos suficientes.
- **Cuenta origen o destino no activa**:
  - El sistema rechaza la operación.
  - Indica que la cuenta se encuentra bloqueada o cerrada.

---

## 4. Casos de uso futuros (fuera de MVP)

Para mantener una visión de evolución del sistema, se documentan algunos casos futuros:

| ID           | Nombre                                      | Estado             |
|--------------|---------------------------------------------|--------------------|
| CU-TAR-001   | Emitir tarjeta de débito/crédito            | Futuro (no MVP)    |
| CU-TAR-002   | Autorizar compra con tarjeta                | Futuro (no MVP)    |
| CU-TAR-003   | Registrar pago de resumen de tarjeta        | Futuro (no MVP)    |
| CU-CLI-004   | Autogestión del cliente vía Home Banking    | Futuro (no MVP)    |

Estos casos de uso no se implementan en la Versión 1, pero sirven como guía para extender
la lógica de negocio bancaria una vez consolidado el núcleo del sistema.
