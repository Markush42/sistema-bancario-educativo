# 02 — Actores del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define los **actores** que interactúan con el sistema bancario educativo, así como
sus **roles**, **objetivos de negocio** y **nivel de participación** en la Versión 1 (MVP).

El foco está puesto en entender cómo funciona un banco desde la programación, por lo que se
priorizan los actores que intervienen en:

- Gestión de empleados.
- Gestión de clientes.
- Gestión de cuentas.
- Operaciones básicas (depósitos, extracciones, transferencias).
- Auditoría mínima de las operaciones.

---

## 2. Resumen de actores

### 2.1 Tabla de actores principales

| ID  | Actor                                 | Tipo      | Descripción breve                                                                 | Interacción con el sistema          | Participa en MVP v1 |
|-----|---------------------------------------|-----------|------------------------------------------------------------------------------------|-------------------------------------|----------------------|
| A1  | Empleado — Cajero                     | Humano    | Empleado operativo de sucursal que registra operaciones sobre cuentas de clientes. | Directa (UI / API interna)          | Sí                   |
| A2  | Empleado — Oficial de Cuentas         | Humano    | Empleado responsable de la relación comercial con el cliente y apertura de cuentas.| Directa (UI / API interna)          | Sí                   |
| A3  | Empleado — Administrador del Sistema  | Humano    | Empleado con permisos elevados para gestionar usuarios internos y parámetros base. | Directa (UI / API interna)          | Sí                   |
| A4  | Cliente                               | Humano    | Persona física o jurídica titular de las cuentas bancarias.                        | Indirecta (a través de empleados)   | Sí (como entidad)    |
| A5  | Auditor Interno                       | Humano    | Responsable de revisar operaciones y trazabilidad del sistema.                     | Indirecta / futura                  | No (futuro)          |
| A6  | Sistema de Autenticación Corporativa  | Sistema   | Servicio corporativo hipotético para login unificado de empleados.                 | Integración técnica (futuro)        | No (futuro)          |
| A7  | Sistema de Tarjetas (red tipo Visa)   | Sistema   | Sistema externo simulado para gestión de tarjetas y autorizaciones.                | Integración técnica (futuro)        | No (futuro)          |
| A8  | Sistema de Clearing / Compensación    | Sistema   | Sistema externo para compensación de transferencias interbancarias.                | Integración técnica (futuro)        | No (futuro)          |

> Nota: En el MVP, el **cliente** es un actor de negocio (aparece en el modelo), pero no
> interactúa con el sistema directamente. Todas las acciones se realizan a través de empleados.

---

## 3. Detalle de actores humanos

### 3.1 Empleado — Cajero (A1)

**Objetivo de negocio**

Permitir que el banco ejecute de forma segura y registrada las operaciones cotidianas de caja:
depósitos, extracciones y consultas de saldo/movimientos.

**Responsabilidades principales**

| Aspecto                 | Detalle                                                                                     |
|-------------------------|---------------------------------------------------------------------------------------------|
| Tipo de tareas          | Operativas, de ventanilla/sucursal.                                                        |
| Operaciones típicas     | Registrar depósitos, extracciones, aplicar transferencias internas.                        |
| Datos que maneja        | Identificación de cliente, cuenta, monto, tipo de operación.                               |
| Alcance de decisión     | Sigue reglas de negocio definidas; no define políticas, solo las aplica.                   |
| Riesgos asociados       | Errores de carga de datos, operaciones sobre cuenta equivocada, montos incorrectos.        |

**Objetivos clave**

- Registrar operaciones respetando las reglas de negocio (saldo suficiente, cuentas válidas, etc.).
- Garantizar que cada operación quede trazada (empleado + fecha/hora + cuenta + monto).
- Minimizar errores y fraudes en la operatoria diaria.

---

### 3.2 Empleado — Oficial de Cuentas (A2)

**Objetivo de negocio**

Gestionar la relación con el cliente, darlo de alta en el sistema y abrir las cuentas necesarias
para operar.

**Responsabilidades principales**

| Aspecto                 | Detalle                                                                                     |
|-------------------------|---------------------------------------------------------------------------------------------|
| Tipo de tareas          | Comerciales y administrativas.                                                              |
| Operaciones típicas     | Alta de clientes, apertura de cuentas, actualización de datos, bloqueo de cliente.         |
| Datos que maneja        | Identidad del cliente, datos de contacto, tipo de cuenta, estado del cliente.              |
| Alcance de decisión     | Puede decidir qué tipo de cuenta ofrecer dentro de un catálogo permitido.                  |
| Riesgos asociados       | Alta de clientes duplicados, datos inconsistentes, cuentas abiertas con datos incorrectos. |

**Objetivos clave**

- Crear y mantener registros de clientes correctos y actualizados.
- Abrir cuentas bancarias bajo las reglas del banco (tipos de cuenta permitidos, estados, etc.).
- Bloquear clientes cuando corresponda (riesgo, fraude, cierre de relación).

---

### 3.3 Empleado — Administrador del Sistema (A3)

**Objetivo de negocio**

Asegurar que solo personas autorizadas puedan acceder al sistema y que los permisos estén
correctamente asignados.

**Responsabilidades principales**

| Aspecto                 | Detalle                                                                                     |
|-------------------------|---------------------------------------------------------------------------------------------|
| Tipo de tareas          | Administrativas y de soporte al sistema.                                                    |
| Operaciones típicas     | Alta/baja de empleados, asignación de roles, bloqueo de cuentas de empleado.               |
| Datos que maneja        | Datos de empleados, roles, estados de acceso.                                               |
| Alcance de decisión     | Define quién puede usar el sistema y con qué nivel de permisos.                            |
| Riesgos asociados       | Asignar permisos incorrectos, dejar cuentas activas de ex-empleados, brechas de seguridad. |

**Objetivos clave**

- Mantener el censo de usuarios internos actualizado.
- Asegurar que los empleados tengan permisos apropiados a su función (cajero, oficial, admin).
- Minimizar accesos indebidos y potencial fraude interno.

---

### 3.4 Cliente (A4)

**Objetivo de negocio**

Ser el titular de las cuentas bancarias y beneficiario de los servicios financieros del banco.

> En el MVP, el cliente **no interactúa directamente** con el sistema; su presencia es de
> dominio (entidad) y las acciones son ejecutadas por empleados.

**Responsabilidades principales (a nivel modelo)**

| Aspecto                 | Detalle                                                                                     |
|-------------------------|---------------------------------------------------------------------------------------------|
| Tipo de actor           | Titular de cuentas (persona física o jurídica).                                             |
| Operaciones vinculadas  | Apertura de cuentas, operaciones sobre sus cuentas (a través de empleados).                 |
| Datos que lo representan| Nombre, documento, datos de contacto, estad
