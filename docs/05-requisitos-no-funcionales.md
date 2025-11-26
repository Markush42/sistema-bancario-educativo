# 05 — Requisitos No Funcionales del Sistema Bancario Educativo

## 1. Objetivo del documento

Este documento especifica los **Requisitos No Funcionales (RNF)** del sistema bancario educativo,
centrados en la **Versión 1 (MVP)** definida en `01-vision-alcance.md`.

Los RNF describen **cómo** debe comportarse el sistema (calidad, restricciones, condiciones de uso),
más allá de **qué** hace (cubierto en `04-requisitos-funcionales.md`).

---

## 2. Convenciones y nomenclatura

- Cada requisito no funcional se identifica como: `RNF-XXX-YYY`, donde:
  - `XXX` indica la categoría (SEC, PERF, DISP, USA, MANT, PORT, AUD).
  - `YYY` es un número correlativo dentro de la categoría.

Ejemplos:
- `RNF-SEC-001` → Requisito de seguridad.
- `RNF-PERF-001` → Requisito de rendimiento.

---

## 3. Requisitos por categoría

---

## 3.1 Seguridad (SEC)

### RNF-SEC-001 — Autenticación obligatoria para empleados

- **ID**: RNF-SEC-001  
- **Nombre**: Autenticación obligatoria  
- **Descripción**:  
  El sistema debe requerir que todo empleado se **autentique** antes de acceder a cualquier
  funcionalidad, incluso de solo lectura (consultas de clientes, cuentas, saldos, etc.).
- **Alcance**:
  - Aplica a todos los módulos: Identidad & Acceso, Clientes, Cuentas, Operaciones, Auditoría.

---

### RNF-SEC-002 — Autorización basada en roles

- **ID**: RNF-SEC-002  
- **Nombre**: Control de acceso por roles  
- **Descripción**:  
  El sistema debe aplicar **control de acceso basado en roles (RBAC)**, asegurando que cada
  empleado solo pueda ejecutar las operaciones que correspondan a sus roles
  (por ejemplo: ADMIN, CAJERO, OFICIAL_CUENTA).
- **Alcance**:
  - Restricción de endpoints/acciones según rol.
  - Visualización de información coherente con permisos.

---

### RNF-SEC-003 — Gestión segura de contraseñas

- **ID**: RNF-SEC-003  
- **Nombre**: Contraseñas no reversibles  
- **Descripción**:  
  El sistema no debe almacenar contraseñas en texto plano. Debe almacenarlas utilizando
  un **algoritmo de hash seguro** (por ejemplo, BCrypt o equivalente), de modo que no sea
  posible recuperar la contraseña original.
- **Alcance**:
  - Módulo de Identidad & Acceso.
  - Base de datos de empleados.

---

### RNF-SEC-004 — Gestión de sesiones

- **ID**: RNF-SEC-004  
- **Nombre**: Sesiones autenticadas  
- **Descripción**:  
  El sistema debe mantener una **sesión autenticada** por empleado, asociada a su identidad
  y roles, y debe invalidar la sesión cuando el usuario cierre sesión o se alcance un tiempo
  máximo de inactividad.
- **Alcance**:
  - Cualquier interfaz (web/API) que mantenga sesiones de usuario.

---

### RNF-SEC-005 — Validación de entrada de datos

- **ID**: RNF-SEC-005  
- **Nombre**: Validación de inputs  
- **Descripción**:  
  El sistema debe validar los datos de entrada en el backend (no solo en el cliente), para
  reducir riesgos de:
  - Inyección de comandos.
  - Datos mal formados (documentos inválidos, montos negativos, etc.).
- **Alcance**:
  - Formularios de alta/modificación de empleados, clientes, cuentas.
  - Operaciones sobre cuentas (depósitos, extracciones, transferencias).

---

### RNF-SEC-006 — Minimización de exposición de datos sensibles

- **ID**: RNF-SEC-006  
- **Nombre**: Exposición limitada de datos sensibles  
- **Descripción**:  
  El sistema debe minimizar la exposición de datos sensibles en pantallas y logs, por ejemplo:
  - No mostrar información confidencial innecesaria (p.ej. contraseñas).
  - No incluir datos críticos en mensajes de error visibles para el usuario final.

---

## 3.2 Rendimiento y capacidad (PERF)

### RNF-PERF-001 — Tiempos de respuesta aceptables

- **ID**: RNF-PERF-001  
- **Nombre**: Tiempo de respuesta en operaciones típicas  
- **Descripción**:  
  En un entorno educativo, el sistema debe responder a las operaciones típicas
  (consulta de saldo, movimientos, alta de cliente, registro de depósito) en un tiempo
  perceptualmente aceptable para el usuario (por ejemplo, < 2 segundos en condiciones normales).
- **Alcance**:
  - Módulos de Clientes, Cuentas, Operaciones.

---

### RNF-PERF-002 — Manejo de carga moderada

- **ID**: RNF-PERF-002  
- **Nombre**: Carga de usuarios concurrentes moderada  
- **Descripción**:  
  El sistema debe poder soportar un número moderado de usuarios concurrentes dentro del contexto
  educativo (por ejemplo, un curso, laboratorio o pequeño equipo de desarrollo), sin degradaciones
  críticas de rendimiento.
- **Alcance**:
  - Concurrencia en consultas y operaciones básicas sobre cuentas y clientes.

---

### RNF-PERF-003 — Eficiencia en operaciones de cuentas

- **ID**: RNF-PERF-003  
- **Nombre**: Operaciones de saldo eficientes  
- **Descripción**:  
  El cálculo de saldos y la aplicación de operaciones (depósitos, extracciones, transferencias)
  debe realizarse de forma eficiente, evitando recomputaciones innecesarias y manteniendo
  la consistencia de saldos sin procesos excesivamente costosos.

---

## 3.3 Disponibilidad, fiabilidad e integridad (DISP)

### RNF-DISP-001 — Disponibilidad básica para entorno educativo

- **ID**: RNF-DISP-001  
- **Nombre**: Disponibilidad en horario de uso  
- **Descripción**:  
  El sistema debe estar disponible durante los horarios normales de trabajo/estudio
  definidos para el entorno educativo (por ejemplo, durante sesiones de laboratorio),
  evitando caídas frecuentes o prolongadas.
- **Alcance**:
  - No se exige alta disponibilidad 24/7 propia de banca real, pero sí estabilidad razonable.

---

### RNF-DISP-002 — Integridad transaccional

- **ID**: RNF-DISP-002  
- **Nombre**: Integridad de operaciones sobre cuentas  
- **Descripción**:  
  El sistema debe preservar la **integridad transaccional** de las operaciones sobre cuentas.
  Una operación financiera debe:
  - Ejecutarse completamente, o
  - No ejecutarse en absoluto (no debe dejar saldos inconsistentes o movimientos “a medias”).
- **Alcance**:
  - Depósitos, extracciones y transferencias internas.

---

### RNF-DISP-003 — Manejo de errores y fallos

- **ID**: RNF-DISP-003  
- **Nombre**: Manejo controlado de errores  
- **Descripción**:  
  Ante errores (por ejemplo, fallo de base de datos, excepción inesperada), el sistema
  debe:
  - Evitar mostrar trazas internas o información sensible al usuario.
  - Registrar el error en un log interno.
  - Presentar un mensaje controlado y entendible.
- **Alcance**:
  - Todas las capas del sistema.

---

## 3.4 Usabilidad (USA)

### RNF-USA-001 — Interfaces comprensibles para empleados

- **ID**: RNF-USA-001  
- **Nombre**: Claridad en la interfaz de usuario  
- **Descripción**:  
  Las interfaces destinadas a empleados deben ser claras y explícitas en cuanto a:
  - Campos obligatorios.
  - Formatos esperados (ej.: montos, documentos).
  - Acciones disponibles.
- **Alcance**:
  - Formularios de alta/edición de clientes, cuentas, operaciones.

---

### RNF-USA-002 — Mensajes de error claros

- **ID**: RNF-USA-002  
- **Nombre**: Mensajes de error orientados al usuario  
- **Descripción**:  
  El sistema debe mostrar mensajes de error **legibles y significativos** para el empleado,
  indicando qué salió mal y, cuando sea posible, cómo corregirlo, sin exponer detalles técnicos internos.
- **Alcance**:
  - Todas las operaciones interactuadas por el usuario.

---

### RNF-USA-003 — Consistencia en la navegación y nomenclatura

- **ID**: RNF-USA-003  
- **Nombre**: Consistencia de diseño  
- **Descripción**:  
  La terminología (cliente, cuenta, movimiento, depósito, extracción, etc.) y los patrones
  de navegación deben ser consistentes en todo el sistema, para reducir errores por confusión.
- **Alcance**:
  - Textos, etiquetas, nombres de acciones, títulos de pantallas.

---

## 3.5 Mantenibilidad y extensibilidad (MANT)

### RNF-MANT-001 — Arquitectura en capas

- **ID**: RNF-MANT-001  
- **Nombre**: Separación por capas  
- **Descripción**:  
  El sistema debe implementarse siguiendo una **arquitectura en capas**, separando al menos:
  - Capa de presentación (API/Interfaces).
  - Capa de aplicación/servicios.
  - Capa de dominio (reglas de negocio).
  - Capa de infraestructura (acceso a datos, integraciones).
- **Objetivo**:
  - Facilitar cambios localizados sin romper todo el sistema.

---

### RNF-MANT-002 — Código legible y documentado

- **ID**: RNF-MANT-002  
- **Nombre**: Legibilidad del código  
- **Descripción**:  
  El código debe ser legible, con nombres claros y comentarios donde sea necesario,
  de forma que otro desarrollador pueda entender la lógica bancaria sin depender del autor original.
- **Alcance**:
  - Clases de dominio, servicios, controladores, repositorios.

---

### RNF-MANT-003 — Tests automatizados mínimos

- **ID**: RNF-MANT-003  
- **Nombre**: Cobertura básica de tests  
- **Descripción**:  
  El sistema debe contar con una **base de tests automatizados mínimos** que cubran:
  - Casos felices de operaciones críticas (depósito, extracción, transferencia).
  - Algunos casos de error (saldo insuficiente, cuenta inactiva, cliente bloqueado).
- **Objetivo**:
  - Facilitar refactors y evolución incremental del sistema.

---

### RNF-MANT-004 — Facilidad de extensión

- **ID**: RNF-MANT-004  
- **Nombre**: Extensibilidad del dominio  
- **Descripción**:  
  El diseño del sistema debe permitir agregar, en versiones futuras, nuevas funcionalidades
  (por ejemplo, módulo de tarjetas, préstamos) sin reescribir por completo el núcleo
  de clientes, cuentas y operaciones.
- **Alcance**:
  - Modelo de dominio.
  - Arquitectura de módulos.

---

## 3.6 Portabilidad y despliegue (PORT)

### RNF-PORT-001 — Entorno de ejecución estándar

- **ID**: RNF-PORT-001  
- **Nombre**: Uso de stack estándar  
- **Descripción**:  
  El sistema debe correr sobre un stack técnico estándar ampliamente disponible
  (por ejemplo, JVM + base de datos relacional), de forma que pueda desplegarse en
  entornos de desarrollo típicos (local, laboratorio, pequeño servidor).
- **Objetivo**:
  - No depender de plataformas propietarias específicas para poder estudiar y desplegar el sistema.

---

### RNF-PORT-002 — Configuración externa

- **ID**: RNF-PORT-002  
- **Nombre**: Configuración externalizada  
- **Descripción**:  
  La configuración del sistema (parámetros de conexión a base de datos, puertos, etc.)
  debe mantenerse **separada del código**, para facilitar:
  - Cambios de entorno (DEV, TEST).
  - Evitar recompilar o redeployar solo por cambios de configuración.
- **Alcance**:
  - Archivos de configuración, variables de entorno, etc.

---

### RNF-PORT-003 — Proceso de despliegue reproducible

- **ID**: RNF-PORT-003  
- **Nombre**: Despliegue repetible  
- **Descripción**:  
  Debe existir una guía o script que permita desplegar el sistema de forma **reproducible**
  en un entorno limpio, minimizando pasos manuales propensos a errores.
- **Alcance**:
  - Documentación de despliegue en `docs/12-plan-sprints-testing-despliegue.md` o equivalente.

---

## 3.7 Logging y Auditoría (AUD)

### RNF-AUD-001 — Nivel de logging mínimo

- **ID**: RNF-AUD-001  
- **Nombre**: Logging de eventos clave  
- **Descripción**:  
  El sistema debe registrar en logs internos, al menos:
  - Errores y excepciones.
  - Eventos de autenticación (éxito/fallo).
  - Operaciones financieras sobre cuentas (alta de movimientos, transferencias).
- **Objetivo**:
  - Facilitar depuración y análisis posterior de incidentes.

---

### RNF-AUD-002 — Separación entre logs técnicos y funcionales

- **ID**: RNF-AUD-002  
- **Nombre**: Distinción de logs  
- **Descripción**:  
  El sistema debe distinguir entre:
  - Logs técnicos (errores de infraestructura, excepciones técnicas).
  - Logs funcionales (operaciones sobre cuentas, alta de clientes).
- **Objetivo**:
  - Permitir que desarrolladores y auditores se enfoquen en la información relevante para su rol.

---

### RNF-AUD-003 — Trazabilidad básica de cambios

- **ID**: RNF-AUD-003  
- **Nombre**: Trazabilidad de cambios críticos  
- **Descripción**:  
  Para operaciones críticas (altas de clientes, aperturas de cuentas, bloqueos, operaciones
  financieras), el sistema debe permitir rastrear:
  - Quién realizó la operación (empleado).
  - Cuándo la realizó (fecha/hora).
  - Qué entidades se vieron afectadas (cliente, cuenta, etc.).

---

## 4. Resumen

Este conjunto de **Requisitos No Funcionales** define el marco de calidad mínimo que el sistema
bancario educativo debe respetar en su MVP:

- Seguridad básica (auth, roles, hash de contraseñas, validación de inputs).
- Rendimiento aceptable en un contexto educativo.
- Integridad transaccional de operaciones bancarias.
- Usabilidad razonable para empleados.
- Mantenibilidad y extensibilidad del código.
- Portabilidad y despliegue reproducible.
- Logging y auditoría coherentes con la naturaleza bancaria del dominio.

Estos RNF complementan a los requisitos funcionales y deben ser considerados en todas
las decisiones de diseño e implementación.
