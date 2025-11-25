# 04 — Requisitos Funcionales del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define los **Requisitos Funcionales (RF)** del sistema bancario educativo,
alineados con:

- La **visión y alcance** definidos en `01-vision-alcance.md`.
- Los **actores** definidos en `02-actores.md`.
- Los **casos de uso de alto nivel** definidos en `03-casos-uso.md`.

El foco está en la **Versión 1 (MVP)**, que cubre:

- Gestión básica de empleados y roles.
- Gestión de clientes.
- Gestión de cuentas bancarias.
- Operaciones básicas sobre cuentas (depósitos, extracciones, transferencias internas).
- Auditoría básica.

---

## 2. Convenciones y nomenclatura

- Cada requisito funcional se identifica como: `RF-XXX`, donde `XXX` es un número correlativo.
- Cada requisito está asociado, cuando aplica, a uno o más **casos de uso (CU)** del documento `03-casos-uso.md`.
- Los requisitos se agrupan por **módulo funcional**:

  - Identidad & Acceso (`AUT`, `EMP`).
  - Clientes (`CLI`).
  - Cuentas Bancarias (`CTA`).
  - Operaciones / Transacciones (`OP`).
  - Auditoría (`AUD`).

---

## 3. Requisitos por módulo

---

### 3.1 Módulo: Identidad & Acceso

#### RF-001 — Autenticación de empleados

- **ID**: RF-001  
- **Nombre**: Autenticación de empleados por usuario y contraseña  
- **Descripción**:  
  El sistema debe permitir que un empleado se autentique mediante usuario y contraseña,
  validando sus credenciales y su estado antes de otorgar acceso.
- **Actor principal**: Empleado (cualquier rol)  
- **Casos de uso relacionados**: CU-AUT-001  
- **Precondiciones (funcionales)**:
  - Debe existir un registro de empleado en el sistema.
  - El empleado debe tener un usuario y contraseña definidos.
- **Postcondiciones**:
  - Si la autenticación es exitosa, se inicia una sesión asociada al empleado y a sus roles.
  - Si falla, no se permite el acceso al sistema.

---

#### RF-002 — Validación de estado de empleado

- **ID**: RF-002  
- **Nombre**: Acceso restringido a empleados activos  
- **Descripción**:  
  El sistema debe permitir el acceso solo a empleados cuyo estado sea **ACTIVO**. Empleados
  en estado BLOQUEADO o INACTIVO no deben poder autenticarse.
- **Actor principal**: Empleado  
- **Casos de uso relacionados**: CU-AUT-001, CU-EMP-003  
- **Regla de negocio clave**:
  - Antes de completar un login exitoso, el sistema debe verificar que el estado del empleado
    sea ACTIVO.

---

#### RF-003 — Registro de intentos de autenticación

- **ID**: RF-003  
- **Nombre**: Registro de intentos de login  
- **Descripción**:  
  El sistema debe registrar todo intento de autenticación, exitoso o fallido, incluyendo:
  - Identificador de empleado (o usuario ingresado).
  - Fecha y hora.
  - Resultado (éxito / fallo).
- **Actor principal**: Empleado  
- **Casos de uso relacionados**: CU-AUT-001, CU-AUD-001  
- **Objetivo**:
  - Proveer información básica para auditoría y análisis de seguridad.

---

#### RF-004 — Alta de empleado

- **ID**: RF-004  
- **Nombre**: Creación de empleados internos  
- **Descripción**:  
  El sistema debe permitir al **Administrador del Sistema** dar de alta nuevos empleados
  definiendo al menos: nombre, usuario, estado inicial, rol/es.
- **Actor principal**: Administrador del Sistema  
- **Casos de uso relacionados**: CU-EMP-001  
- **Reglas de negocio**:
  - El nombre de usuario debe ser único.
  - El estado inicial recomendado es ACTIVO.

---

#### RF-005 — Asignación y actualización de roles de empleado

- **ID**: RF-005  
- **Nombre**: Gestión de roles de empleados  
- **Descripción**:  
  El sistema debe permitir al **Administrador del Sistema** asignar, modificar o retirar
  roles de un empleado (por ejemplo: ADMIN, CAJERO, OFICIAL_CUENTA).
- **Actor principal**: Administrador del Sistema  
- **Casos de uso relacionados**: CU-EMP-002  
- **Reglas de negocio**:
  - Un empleado puede tener uno o varios roles.
  - Los permisos disponibles dependen del rol asignado.

---

#### RF-006 — Bloqueo de empleados

- **ID**: RF-006  
- **Nombre**: Bloqueo de acceso de empleados  
- **Descripción**:  
  El sistema debe permitir al **Administrador del Sistema** cambiar el estado de un empleado
  a BLOQUEADO para impedir que vuelva a autenticarse y realizar operaciones.
- **Actor principal**: Administrador del Sistema  
- **Casos de uso relacionados**: CU-EMP-003  
- **Reglas de negocio**:
  - Un empleado BLOQUEADO debe ser rechazado en el proceso de autenticación (relacionado con RF-002).

---

### 3.2 Módulo: Clientes

#### RF-010 — Alta de cliente

- **ID**: RF-010  
- **Nombre**: Registro de nuevos clientes  
- **Descripción**:  
  El sistema debe permitir al **Oficial de Cuentas** dar de alta nuevos clientes
  con datos mínimos obligatorios (ej.: nombre, apellido, documento de identidad, datos de contacto).
- **Actor principal**: Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CLI-001  
- **Reglas de negocio**:
  - El documento de identidad del cliente (DNI/CUIT/ID equivalente) debe ser único.
  - El estado inicial del cliente debe ser **ACTIVO**.

---

#### RF-011 — Actualización de datos de cliente

- **ID**: RF-011  
- **Nombre**: Modificación de datos de clientes  
- **Descripción**:  
  El sistema debe permitir al **Oficial de Cuentas** actualizar los datos de un cliente
  existente (domicilio, contacto, etc.), manteniendo la trazabilidad de los cambios
  cuando sea relevante.
- **Actor principal**: Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CLI-002  

---

#### RF-012 — Bloqueo de clientes

- **ID**: RF-012  
- **Nombre**: Bloqueo de clientes para nuevas operaciones  
- **Descripción**:  
  El sistema debe permitir cambiar el estado de un cliente a **BLOQUEADO**, impidiendo
  que se abran nuevas cuentas para él o se realicen nuevas operaciones sobre sus cuentas.
- **Actor principal**: Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CLI-003  
- **Reglas de negocio**:
  - Un cliente en estado BLOQUEADO no debe poder:
    - Abrir nuevas cuentas (afecta RF-020).
    - Ser origen o destino de nuevas operaciones (relacionado con RF-030, RF-031, RF-032).

---

#### RF-013 — Consulta básica de clientes

- **ID**: RF-013  
- **Nombre**: Búsqueda y consulta de clientes  
- **Descripción**:  
  El sistema debe permitir buscar clientes por criterios básicos (documento, nombre, estado)
  y visualizar su información principal.
- **Actor principal**: Oficial de Cuentas, Cajero (según permisos)  
- **Casos de uso relacionados**: CU-CLI-001, CU-CLI-002  

---

### 3.3 Módulo: Cuentas Bancarias

#### RF-020 — Apertura de cuenta bancaria

- **ID**: RF-020  
- **Nombre**: Apertura de cuenta para cliente existente  
- **Descripción**:  
  El sistema debe permitir al **Oficial de Cuentas** abrir una nueva cuenta bancaria
  para un cliente existente y ACTIVO, seleccionando el tipo de cuenta dentro
  de un catálogo permitido.
- **Actor principal**: Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CTA-001  
- **Reglas de negocio**:
  - El cliente debe existir y estar en estado ACTIVO (relacionado con RF-010, RF-012).
  - El tipo de cuenta debe estar dentro del catálogo admitido (ej.: CAJA_AHORRO, CUENTA_CORRIENTE).
  - La cuenta se crea con saldo inicial 0 y estado **ACTIVA**.

---

#### RF-021 — Generación de número de cuenta único

- **ID**: RF-021  
- **Nombre**: Asignación de número de cuenta  
- **Descripción**:  
  El sistema debe asignar automáticamente un **número de cuenta único** a cada cuenta
  bancaria creada.
- **Actor principal**: Sistema  
- **Casos de uso relacionados**: CU-CTA-001  

---

#### RF-022 — Consulta de saldo de cuenta

- **ID**: RF-022  
- **Nombre**: Consulta de saldo actual  
- **Descripción**:  
  El sistema debe permitir a empleados autorizados (Cajero, Oficial de Cuentas) consultar
  el saldo actual de una cuenta bancaria.
- **Actor principal**: Cajero, Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CTA-002  
- **Reglas de negocio**:
  - Solo empleados autenticados pueden consultar saldos (relacionado con RF-001, RF-002).

---

#### RF-023 — Consulta de movimientos de cuenta

- **ID**: RF-023  
- **Nombre**: Historial de movimientos  
- **Descripción**:  
  El sistema debe permitir consultar la lista de movimientos de una cuenta, mostrando
  al menos: fecha/hora, tipo de movimiento (DEBITO/CRÉDITO), monto, saldo posterior
  y descripción/concepto.
- **Actor principal**: Cajero, Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CTA-003, CU-AUD-001  

---

#### RF-024 — Estados de la cuenta y restricciones de operación

- **ID**: RF-024  
- **Nombre**: Manejo de estados de cuenta  
- **Descripción**:  
  El sistema debe manejar al menos los siguientes estados de cuenta:
  - ACTIVA
  - BLOQUEADA
  - CERRADA  
  Y aplicar restricciones según el estado.
- **Actor principal**: Sistema, Oficial de Cuentas  
- **Casos de uso relacionados**: CU-CTA-001, CU-OP-001, CU-OP-002, CU-OP-003  
- **Reglas de negocio**:
  - Solo cuentas ACTIVA pueden recibir depósitos, extracciones o transferencias.
  - Cuentas BLOQUEADA no pueden recibir nuevas operaciones, pero su historial sigue consultable.
  - Cuentas CERRADA no pueden recibir ni emitir operaciones nuevas.

---

### 3.4 Módulo: Operaciones / Transacciones

#### RF-030 — Registro de depósitos en cuenta

- **ID**: RF-030  
- **Nombre**: Depósito en cuenta bancaria  
- **Descripción**:  
  El sistema debe permitir registrar depósitos en cuentas bancarias, actualizando el saldo
  de la cuenta y generando un movimiento de tipo **CRÉDITO**.
- **Actor principal**: Cajero  
- **Casos de uso relacionados**: CU-OP-001  
- **Reglas de negocio**:
  - La cuenta debe existir y estar ACTIVA (relacionado con RF-024).
  - El monto del depósito debe ser mayor que cero.
  - Tras el depósito, el sistema debe:
    - Actualizar el saldo de la cuenta.
    - Crear un movimiento con:
      - Fecha/hora.
      - Tipo CRÉDITO.
      - Monto.
      - Saldo posterior.
      - Identificador del empleado que ejecutó la operación.

---

#### RF-031 — Registro de extracciones de cuenta

- **ID**: RF-031  
- **Nombre**: Extracción de fondos de cuenta bancaria  
- **Descripción**:  
  El sistema debe permitir registrar extracciones de fondos desde una cuenta bancaria,
  actualizando el saldo y generando un movimiento de tipo **DEBITO**, siempre que el saldo
  sea suficiente.
- **Actor principal**: Cajero  
- **Casos de uso relacionados**: CU-OP-002  
- **Reglas de negocio**:
  - La cuenta debe existir y estar ACTIVA.
  - El monto de extracción debe ser mayor que cero.
  - El saldo de la cuenta debe ser suficiente para cubrir el monto (sin descubierto avanzado en el MVP).
  - Tras la extracción:
    - El saldo de la cuenta se disminuye.
    - Se registra un movimiento de tipo DEBITO con los datos correspondientes.

---

#### RF-032 — Registro de transferencias internas

- **ID**: RF-032  
- **Nombre**: Transferencia interna entre cuentas  
- **Descripción**:  
  El sistema debe permitir mover fondos de una cuenta origen a una cuenta destino dentro
  del mismo banco, registrando movimientos en ambas cuentas.
- **Actor principal**: Cajero, Oficial de Cuentas  
- **Casos de uso relacionados**: CU-OP-003  
- **Reglas de negocio**:
  - Tanto la cuenta origen como la destino deben existir y estar ACTIVA.
  - Las cuentas origen y destino deben ser distintas.
  - La cuenta origen debe tener saldo suficiente.
  - La operación debe:
    - Disminuir el saldo de la cuenta origen (DEBITO).
    - Aumentar el saldo de la cuenta destino (CRÉDITO).
    - Crear dos movimientos vinculados por un identificador común de operación.

---

#### RF-033 — Consistencia de saldos en operaciones

- **ID**: RF-033  
- **Nombre**: Consistencia de saldos por operación  
- **Descripción**:  
  El sistema debe garantizar que, ante cualquier operación (depósito, extracción,
  transferencia interna), el saldo de la cuenta refleje correctamente la aplicación
  de los movimientos generados.
- **Actor principal**: Sistema  
- **Casos de uso relacionados**: CU-OP-001, CU-OP-002, CU-OP-003  
- **Reglas de negocio** (nivel funcional):
  - No deben quedar estados intermedios en los que el saldo no coincida con la suma
    de movimientos aplicados.
  - En una transferencia interna, ambos movimientos (origen y destino) deben considerarse
    parte de una misma operación lógica.

---

### 3.5 Módulo: Auditoría

#### RF-040 — Registro de operaciones financieras

- **ID**: RF-040  
- **Nombre**: Auditoría de operaciones de cuenta  
- **Descripción**:  
  El sistema debe registrar cada operación financiera realizada sobre una cuenta bancaria
  (depósito, extracción, transferencia), almacenando al menos:
  - Identificador de la cuenta.
  - Tipo de operación.
  - Monto.
  - Fecha/hora.
  - Identificador del empleado que ejecutó la operación.
  - Identificador de operación cuando corresponda (por ejemplo, para transferencias).
- **Actor principal**: Sistema  
- **Casos de uso relacionados**: CU-OP-001, CU-OP-002, CU-OP-003, CU-AUD-001  

---

#### RF-041 — Registro de eventos de autenticación

- **ID**: RF-041  
- **Nombre**: Auditoría de accesos al sistema  
- **Descripción**:  
  El sistema debe registrar eventos relevantes de acceso:
  - Login exitoso.
  - Login fallido.
  - Bloqueo de cuentas de empleado.
- **Actor principal**: Sistema  
- **Casos de uso relacionados**: CU-AUT-001, CU-EMP-003, CU-AUD-001  

---

#### RF-042 — Consulta de trazabilidad de operaciones

- **ID**: RF-042  
- **Nombre**: Consulta de trazabilidad básica  
- **Descripción**:  
  El sistema debe permitir consultar, para una operación específica o un conjunto de
  operaciones filtradas, al menos:
  - Qué cuenta fue afectada.
  - Qué empleado ejecutó la operación.
  - Fecha y hora.
  - Tipo y monto.
- **Actor principal**: Administrador del Sistema, Auditor Interno (futuro)  
- **Casos de uso relacionados**: CU-AUD-001  

---

## 4. Tabla de trazabilidad (Resumen CU ↔ RF)

| Caso de Uso    | Requisitos funcionales relacionados                          |
|----------------|--------------------------------------------------------------|
| CU-AUT-001     | RF-001, RF-002, RF-003, RF-041                               |
| CU-EMP-001     | RF-004                                                       |
| CU-EMP-002     | RF-005                                                       |
| CU-EMP-003     | RF-006, RF-002, RF-041                                       |
| CU-CLI-001     | RF-010, RF-013                                               |
| CU-CLI-002     | RF-011, RF-013                                               |
| CU-CLI-003     | RF-012, RF-013, RF-024                                       |
| CU-CTA-001     | RF-020, RF-021, RF-024                                       |
| CU-CTA-002     | RF-022                                                       |
| CU-CTA-003     | RF-023, RF-040, RF-042                                       |
| CU-OP-001      | RF-030, RF-033, RF-040                                       |
| CU-OP-002      | RF-031, RF-033, RF-040                                       |
| CU-OP-003      | RF-032, RF-033, RF-040                                       |
| CU-AUD-001     | RF-003, RF-040, RF-041, RF-042                               |

---

Este documento sirve como base directa para:

- El diseño de **servicios de aplicación** (interfaces y métodos).
- La definición de **tests funcionales**.
- La implementación futura del backend (por ejemplo, en Java/Spring).
