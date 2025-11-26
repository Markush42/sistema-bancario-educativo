# 10 — Arquitectura en Capas del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento define la **arquitectura en capas** del sistema bancario educativo, tomando como base:

- La **visión del sistema** (`01-vision-alcance.md`).
- Las **entidades de dominio** (`07-entidades-dominio.md`).
- El **modelo conceptual** (`09-modelo-conceptual.md`).
- Los **requisitos funcionales y no funcionales** (`04-requisitos-funcionales.md`, `05-requisitos-no-funcionales.md`).

El objetivo es:

- Aclarar **qué responsabilidades tiene cada capa**.
- Definir **cómo se comunican las capas entre sí**.
- Establecer **reglas de dependencia** para mantener un diseño mantenible y extensible.

Esta arquitectura está pensada para un backend típico (por ejemplo, en Java sobre la JVM), pero es lo suficientemente general como para poder adaptarse a otras tecnologías.

---

## 2. Principios de diseño

La arquitectura propuesta se basa en los siguientes principios:

1. **Separación de responsabilidades (SoC)**  
   Cada capa tiene responsabilidades claras y no debe asumir trabajo de otras capas.

2. **Dependencias dirigidas hacia el dominio**  
   El **dominio bancario** es el centro. Las demás capas existen para exponerlo, orquestarlo o persistirlo.

3. **Bajo acoplamiento y alta cohesión**  
   - Las capas deben estar lo menos acopladas posible entre sí.  
   - Dentro de cada capa, los componentes deben agruparse por responsabilidades coherentes.

4. **Independencia de frameworks**  
   En lo posible, el código de **dominio** no debe depender de frameworks (web, persistencia, etc.).  
   Esto facilita el testeo, el cambio de frameworks y la evolución del sistema.

5. **Claridad sobre qué NO hace cada capa**  
   Tan importante como definir las responsabilidades es dejar claro qué cosas no deben hacerse en esa capa.

---

## 3. Vista general de las capas

El sistema se organiza en cuatro capas principales:

1. **Capa de Presentación / Interfaz (Interface / Web / API)**  
   - Expone el sistema a actores externos (empleados, sistemas).  
   - Traduce HTTP/JSON u otros protocolos al lenguaje de la aplicación.

2. **Capa de Aplicación (Application / Services de aplicación)**  
   - Orquesta casos de uso.  
   - Coordina el trabajo entre el dominio y la infraestructura.  
   - No contiene lógica bancaria compleja, sino reglas de orquestación.

3. **Capa de Dominio (Domain)**  
   - Contiene el **modelo bancario**: entidades, valores, lógica de negocio.  
   - Conoce las reglas de negocio: saldos, estados de cuenta, restricciones, invariantes.

4. **Capa de Infraestructura (Infrastructure)**  
   - Implementa detalles técnicos: persistencia (base de datos), integraciones externas, logging, etc.  
   - Provee implementaciones concretas para interfaces usadas por la capa de dominio y aplicación.

De forma transversal, existen **preocupaciones cross-cutting**:

- Seguridad / Autenticación / Autorización.  
- Logging y auditoría.  
- Manejo de errores.

Estas preocupaciones se implementan apoyándose en las capas anteriores, sin romper las reglas de dependencia.

---

## 4. Esquema general de capas (visión textual)

Representación simplificada de las capas y sus dependencias:

    [ Capa de Presentación / API ]
               ↓
    [ Capa de Aplicación ]
               ↓
    [ Capa de Dominio ]
               ↓
    [ Capa de Infraestructura ]

**Regla principal de dependencia:**

- Cada capa solo puede depender de la capa inmediatamente **inferior**, nunca de una capa “superior”.
- La **capa de dominio** no debe depender directamente de detalles de infraestructura
  (por ejemplo, clases concretas de ORM).

---

## 5. Detalle por capa

### 5.1 Capa de Presentación / Interfaz

**Responsabilidad principal**

- Recibir solicitudes externas (por ejemplo, HTTP) y convertirlas en **llamadas a la capa de aplicación**.
- Enviar respuestas formateadas (ej.: JSON) basadas en los resultados de la capa de aplicación.

**Ejemplos de componentes**

- Controladores / Endpoints (ej.: `EmpleadoController`, `ClienteController`, `CuentaController`, `OperacionController`).
- DTOs de entrada/salida específicos de la interfaz (request/response).

**Qué hace**

- Valida que la request tenga la estructura correcta (campos requeridos, formatos básicos).  
- Llama a los servicios de aplicación apropiados.  
- Traduce excepciones técnicas en respuestas HTTP adecuadas (4xx, 5xx).

**Qué NO hace**

- No implementa lógica de negocio bancaria (ej.: cómo se calcula un saldo, reglas de transferencia).  
- No habla directamente con la base de datos.  
- No implementa transacciones de negocio; delega en capas inferiores.

---

### 5.2 Capa de Aplicación

**Responsabilidad principal**

- Implementar **casos de uso** del sistema (por ejemplo: “Registrar Depósito”, “Abrir Cuenta”, “Registrar Transferencia Interna”).  
- Coordinar la interacción entre:
  - Dominio (entidades y lógica).  
  - Infraestructura (repositorios, eventos de auditoría, etc.).  
  - Otras capas de aplicación si aplica.

**Ejemplos de componentes**

- Servicios de aplicación:
  - `EmpleadoAppService`  
  - `ClienteAppService`  
  - `CuentaAppService`  
  - `OperacionAppService`
- DTOs de aplicación (posiblemente diferentes de los DTOs de presentación).

**Qué hace**

- Maneja el **flujo** de un caso de uso:
  - Validaciones a nivel de caso (ej.: “cliente debe estar ACTIVO para abrir cuenta”).  
  - Invoca métodos del dominio para ejecutar reglas de negocio.  
  - Llama a repositorios para obtener o persistir entidades.  
  - Genera eventos de auditoría según sea necesario.
- Controla transacciones de negocio (a nivel de servicio).

**Qué NO hace**

- No contiene las reglas de negocio detalladas (por ejemplo, cómo impacta exactamente un movimiento en el saldo).  
- No implementa consultas directas a la base de datos (eso se delega a infraestructura).  
- No formatea respuestas HTTP (eso es tarea de la presentación).

---

### 5.3 Capa de Dominio

**Responsabilidad principal**

- Encapsular el **corazón del negocio bancario**:
  - Entidades (`Empleado`, `Cliente`, `CuentaBancaria`, `MovimientoCuenta`, `OperacionFinanciera`, `EventoAuditoria`).  
  - Lógica y reglas de negocio.  
  - Invariantes que se deben cumplir siempre.

**Ejemplos de componentes**

- Entidades de dominio:
  - `Empleado`  
  - `Cliente`  
  - `CuentaBancaria`  
  - `MovimientoCuenta`  
  - `OperacionFinanciera`  
  - `EventoAuditoria`
- Objetos de valor (si se usan).  
- Interfaces de repositorios (no implementaciones):
  - `CuentaRepository`  
  - `ClienteRepository`  
  - `EmpleadoRepository`  
  - `OperacionRepository`  
  - `MovimientoRepository`

**Qué hace**

- Define **métodos** que representan operaciones de negocio, por ejemplo:
  - `CuentaBancaria.registrarDeposito(monto)`  
  - `CuentaBancaria.registrarExtraccion(monto)`  
  - `OperacionFinanciera.crearTransferencia(...)`
- Verifica invariantes de negocio:
  - No permitir extracciones con saldo insuficiente.  
  - No permitir operaciones sobre cuentas BLOQUEADAS o CERRADAS.  
  - Mantener consistencia entre movimientos y saldo.

**Qué NO hace**

- No maneja protocolos (HTTP, JSON, etc.).  
- No conoce detalles de la base de datos, ni de cómo se mapean las entidades a tablas.  
- No depende de frameworks concretos (idealmente).

---

### 5.4 Capa de Infraestructura

**Responsabilidad principal**

- Implementar detalles técnicos necesarios para que el sistema funcione en un entorno real.

**Ejemplos de componentes**

- Implementaciones de repositorios:
  - `JpaCuentaRepository`  
  - `JpaClienteRepository`  
  - `JpaEmpleadoRepository`
- Mapeos ORM (por ejemplo, anotaciones JPA/Hibernate).  
- Integraciones con:
  - Base de datos.  
  - Sistemas externos (si existiera alguno en versiones futuras).
- Adaptadores para logging y auditoría:
  - `EventoAuditoriaRepository` y su implementación.
- Configuración técnica (conexión a BD, pooling, etc.).

**Qué hace**

- Implementa las interfaces de repositorios definidas en el dominio.  
- Gestiona la persistencia y recuperación de entidades.  
- Apoya funcionalidades transversales (logging, auditoría) a través de mecanismos técnicos.

**Qué NO hace**

- No decide reglas de negocio.  
- No debería contener lógica de negocio bancaria (más allá de pequeñas optimizaciones o filtros técnicos).  
- No debe invocarse directamente desde la capa de presentación (siempre se pasa por la capa de aplicación).

---

## 6. Ejemplo de flujo entre capas: “Registrar transferencia interna”

Flujo simplificado para el caso de uso **“Registrar transferencia interna entre cuentas”**:

1. **Capa de Presentación**
   - Endpoint: `POST /api/operaciones/transferencias`.  
   - Request JSON incluye:
     - `cuentaOrigenId`  
     - `cuentaDestinoId`  
     - `monto`
   - Se validan campos básicos (no nulos, formato numérico).

2. **Capa de Aplicación (`OperacionAppService`)**
   - Recibe los datos validados desde el controlador.  
   - Verifica:
     - Que las cuentas origen y destino existan (usando repositorios).  
     - Que no sean la misma cuenta.
   - Pide a los repositorios cargar las entidades `CuentaBancaria` involucradas.  
   - Llama a la lógica de dominio:
     - `OperacionFinanciera.crearTransferencia(cuentaOrigen, cuentaDestino, monto, empleado)`
     - Métodos de las cuentas:
       - `cuentaOrigen.registrarDebito(monto)`  
       - `cuentaDestino.registrarCredito(monto)`
   - Coordina la persistencia:
     - Guarda la `OperacionFinanciera`.  
     - Guarda los `MovimientoCuenta`.  
     - Actualiza saldos de las cuentas.  
   - Crea un `EventoAuditoria` asociado.

3. **Capa de Dominio**
   - `OperacionFinanciera` verifica:
     - Que el monto sea válido (> 0).  
     - Que las cuentas estén ACTIVAS.
   - `CuentaBancaria` verifica:
     - En el débito, que haya saldo suficiente.
   - Se generan objetos `MovimientoCuenta` coherentes.

4. **Capa de Infraestructura**
   - Implementaciones de repositorios (por ejemplo, JPA) persisten:
     - La operación.  
     - Los movimientos.  
     - Las cuentas actualizadas.
   - Se registran logs técnicos si es necesario.

5. **Respuesta**
   - La capa de aplicación devuelve un resultado al controlador
     (por ejemplo, un DTO con resumen de operación).  
   - La capa de presentación transforma ese resultado a JSON y responde al cliente.

---

## 7. Reglas de dependencia entre capas

Para mantener una arquitectura limpia, se establecen las siguientes reglas:

1. **Presentación → Aplicación → Dominio → Infraestructura**  
   El flujo de dependencias debe ser **unidireccional** hacia abajo.

2. La **Capa de Dominio**:
   - Puede definir **interfaces** que la infraestructura implementa.  
   - No debe depender de clases concretas de infraestructura.

3. La **Capa de Presentación**:
   - No debe acceder directamente a repositorios.  
   - No debe acceder directamente a entidades de infraestructura (ORM, etc.).

4. La **Capa de Aplicación**:
   - Orquesta, pero no saltea capas:
     - No llama directamente a detalles técnicos (por ejemplo, `EntityManager`).  
     - Utiliza siempre los repositorios y servicios definidos.

---

## 8. Sugerencia de organización de paquetes (ejemplo)

A nivel de proyecto (por ejemplo, Java), se podría adoptar una estructura similar a:

    com.bancoeducativo
     ├── presentation      (controladores, DTOs de request/response)
     ├── application       (servicios de aplicación, casos de uso)
     ├── domain            (entidades de dominio, repositorios como interfaces)
     └── infrastructure    (impl de repositorios, mapeos ORM, config técnica)

Dentro de cada capa, se pueden organizar subpaquetes por módulo funcional:

- `empleados`  
- `clientes`  
- `cuentas`  
- `operaciones`  
- `auditoria`

---

## 9. Consideraciones para pruebas

La arquitectura en capas facilita distintos tipos de tests:

- **Capa de Dominio**:
  - Tests unitarios puros.  
  - Sin frameworks ni base de datos.  
  - Se testean reglas de negocio (saldos, estados, invariantes).

- **Capa de Aplicación**:
  - Tests de integración a nivel de casos de uso.  
  - Se pueden usar repositorios en memoria o mocks.

- **Capa de Presentación**:
  - Tests de endpoints (por ejemplo, tests HTTP simulados).  
  - Verificación de validaciones y códigos de estado.

- **Capa de Infraestructura**:
  - Tests de integración con la base de datos (repositorios reales).  
  - Verificación de mapeos ORM.

---

## 10. Próximos pasos

Con la arquitectura en capas definida, los siguientes pasos serán:

1. Detallar **módulos y responsabilidades** específicas por contexto funcional en  
   `11-modulos-responsabilidades.md`.  
2. Alinear la **estructura real del proyecto** (paquetes, módulos) con esta arquitectura.  
3. Definir una estrategia inicial de:
   - Controladores / endpoints por módulo.  
   - Servicios de aplicación principales.  
   - Interfaces de repositorios en el dominio y sus implementaciones en infraestructura.

Este documento sirve como **guía de alto nivel** para que todo el código del sistema bancario educativo
se mantenga organizado, coherente y alineado con el dominio bancario definido.
