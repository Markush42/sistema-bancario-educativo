# 01 — Visión y Alcance del Sistema Bancario Educativo

## 1. Contexto y motivación

Este proyecto define un **sistema bancario educativo** pensado para entender, mediante programación,
cómo funciona un banco desde cero: cómo se modelan **clientes**, **empleados**, **cuentas** y
**operaciones** (depósitos, extracciones, transferencias), y cómo se registran estos movimientos
de forma consistente.

El objetivo no es replicar toda la complejidad de un banco real (Santander, Visa, etc.), sino
construir un **núcleo bancario simple y claro** que sirva como base para aprender:

- Diseño de software y arquitectura en capas.
- Modelado de dominio (entidades y relaciones).
- Reglas de negocio típicas de un banco.
- Buenas prácticas de desarrollo backend.

---

## 2. Objetivo del sistema

El sistema tiene un **doble objetivo**:

1. **Objetivo pedagógico**

   - Permitir comprender, mediante código, las piezas fundamentales de un banco:
     - Clientes, empleados, cuentas bancarias y movimientos.
     - Operaciones básicas: depósito, extracción, transferencia interna.
     - Registro de auditoría de quién hace qué y cuándo.
   - Servir como base para ir incorporando complejidad realista en iteraciones posteriores
     (tarjetas, préstamos, integraciones, etc.).

2. **Objetivo funcional básico**

   - Proveer un **backoffice interno** para empleados del banco, donde:
     - Se gestionen clientes y sus cuentas.
     - Se registren operaciones sobre las cuentas.
     - Se pueda consultar el estado actual (saldo) y el historial de movimientos de cada cuenta.

---

## 3. Tipo de sistema elegido

**Tipo de sistema:**

> **Sistema de Gestión Bancaria Interna (Core Bancario + Backoffice para empleados)**

Características principales:

- Uso interno, orientado a **empleados** (no a clientes finales en esta versión).
- Se centra en el **núcleo bancario**:
  - Modelo de clientes.
  - Modelo de cuentas bancarias.
  - Modelo de movimientos / transacciones.
- Expone una interfaz interna (API / UI para empleados) para:
  - Alta y gestión de clientes.
  - Apertura y gestión de cuentas.
  - Registro de operaciones.
  - Consultas de saldo y movimientos.

---

## 4. Alcance del MVP — Versión 1

La **Versión 1 (MVP)** es la mínima versión funcional que permite experimentar con la lógica
bancaria básica desde código.

### 4.1 Funcionalidades incluidas

1. **Gestión de empleados (básica)**
   - Registro de empleados internos del banco.
   - Rol simple por empleado (ADMIN, OPERADOR).
   - Autenticación mínima para usar el sistema.

2. **Gestión de clientes**
   - Alta de clientes con datos básicos (nombre, documento, contacto).
   - Modificación de datos de clientes.
   - Bloqueo / inhabilitación de clientes.

3. **Gestión de cuentas bancarias**
   - Apertura de cuentas bancarias para un cliente:
     - `CAJA_AHORRO`
     - `CUENTA_CORRIENTE` (sin descubierto complejo en el MVP).
   - Consulta de datos de la cuenta:
     - Número de cuenta, tipo, saldo actual, estado (ACTIVA / BLOQUEADA / CERRADA).

4. **Operaciones básicas sobre cuentas**
   - **Depósito en cuenta**:
     - Aumenta el saldo.
     - Registra un movimiento de crédito.
   - **Extracción de cuenta**:
     - Disminuye el saldo.
     - Valida saldo suficiente (sin descubierto avanzado):
     - Registra un movimiento de débito.
   - **Transferencia interna** (entre cuentas del mismo banco):
     - Valida cuenta origen y destino.
     - Valida saldo suficiente en cuenta origen.
     - Registra dos movimientos: débito en origen, crédito en destino.

5. **Consulta de saldo y movimientos**
   - Consulta del saldo actual de una cuenta.
   - Listado de movimientos (fecha, tipo, monto, saldo posterior, descripción).

6. **Auditoría básica**
   - Registro de qué empleado ejecutó cada operación.
   - Registro de fecha y hora de las operaciones.
   - Capacidad de rastrear quién hizo qué sobre una cuenta.

### 4.2 Actores considerados en el MVP

- **Empleado**: usa el sistema para gestionar clientes, cuentas y operaciones.
- **Administrador**: configura empleados y roles básicos.

> El cliente existe como entidad de negocio, pero no interactúa directamente con el sistema en esta versión.

---

## 5. Fuera de alcance (Versión 1)

Para mantener el enfoque en lo esencial, en **esta Versión 1** se excluye explícitamente:

1. **Canales para clientes finales**
   - Home Banking web, app móvil.
   - Cajeros automáticos (ATM).
   - Integración con POS, QR, etc.

2. **Módulo de tarjetas tipo Visa**
   - Emisión y gestión detallada de tarjetas de crédito/débito.
   - Motor de autorización de compras en tiempo real.
   - Cierre de resumen, intereses, financiación de saldos.

3. **Productos financieros avanzados**
   - Préstamos de todo tipo.
   - Plazos fijos, fondos comunes de inversión.
   - Intereses complejos, tasas variables.

4. **Aspectos regulatorios complejos**
   - Reglas AML/KYC completas.
   - Reportes regulatorios oficiales.

5. **Escalabilidad productiva real**
   - Clustering, microservicios, balanceadores en producción.
   - Replicación/particionamiento de base de datos.

6. **Multi-moneda y multi-país**
   - Soporte de múltiples monedas y tipos de cambio.
   - Reglas fiscales específicas por país.

---
