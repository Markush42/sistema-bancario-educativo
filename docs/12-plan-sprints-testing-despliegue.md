# 12 — Plan de Sprints, Testing y Despliegue

## 1. Objetivo del documento

Este documento define el **plan de trabajo iterativo** para construir el MVP del
Sistema Bancario Educativo, incluyendo:

- **Sprints** (iteraciones de desarrollo) y sus objetivos.
- La **estrategia de testing** (qué se prueba y cuándo).
- La **estrategia de despliegue** (cómo se ejecuta la app y cómo se valida).

Está pensado para que el proyecto pueda avanzar de forma **incremental** y **controlada**,
siguiendo buenas prácticas de ingeniería de software.

---

## 2. Vista general del roadmap del MVP

A nivel alto, el MVP se construirá en **3 sprints principales**, cada uno con foco en
un conjunto de módulos:

| Sprint | Foco principal                                               |
|--------|--------------------------------------------------------------|
| S1     | Base del proyecto, dominio y operaciones mínimas de lectura  |
| S2     | Operaciones financieras básicas y coherencia de saldos       |
| S3     | Auditoría, endurecimiento, pruebas integrales y despliegue   |

Duración sugerida (flexible):  
- 1 sprint = 1 a 2 semanas, según tiempo disponible.

---

## 3. Plan de sprints

### 3.1 Sprint 1 — Fundaciones del sistema y lectura básica

**Objetivo principal**

Levantar el esqueleto del sistema con una base sólida:

- Proyecto backend creado.
- Arquitectura de capas aplicada.
- Dominio bancario modelado.
- Primeros endpoints de lectura funcionando.

**Módulos y alcance**

- Arquitectura básica:
  - Paquetes `presentation`, `application`, `domain`, `infrastructure`.
- Dominio:
  - Entidades principales: `Empleado`, `Cliente`, `CuentaBancaria`.
- Infraestructura de persistencia:
  - Configuración de base de datos.
  - Repositorios básicos de lectura/escritura.
- Endpoints de lectura:
  - Consultar cliente por ID.
  - Consultar cuenta por ID y saldo.

**Entregables del Sprint 1**

- Proyecto creado (por ejemplo, Spring Boot) con estructura de capas.
- Entidades de dominio mapeadas a la base de datos.
- Repositorios implementados para:
  - `Cliente`
  - `CuentaBancaria`
- Endpoints iniciales:
  - GET `/api/clientes/{id}`
  - GET `/api/cuentas/{id}/saldo`
- Documentación actualizada:
  - Readme técnico básico de cómo correr el proyecto en local.
  - Confirmación de que el diseño real respeta los documentos 07–11.

**Criterios de aceptación**

- La aplicación levanta en entorno local y se conecta a la base de datos.
- Se puede consultar un cliente y una cuenta de prueba vía HTTP (por ejemplo con Postman).
- Pruebas unitarias básicas del dominio (por ejemplo, creación de entidades).

---

### 3.2 Sprint 2 — Operaciones financieras básicas

**Objetivo principal**

Implementar las **operaciones de negocio centrales**:

- Depósitos.
- Extracciones.
- Transferencias internas.

Además de asegurar la coherencia del saldo y los movimientos.

**Módulos y alcance**

- Dominio:
  - `MovimientoCuenta`
  - `OperacionFinanciera`
  - Métodos de negocio en `CuentaBancaria`:
    - `registrarCredito`
    - `registrarDebito` (con verificación de saldo).
- Aplicación:
  - `OperacionAppService` (o equivalente).
- Presentación:
  - Endpoints:
    - POST `/api/operaciones/depositos`
    - POST `/api/operaciones/extracciones`
    - POST `/api/operaciones/transferencias`
- Infraestructura:
  - Repositorios de `MovimientoCuenta` y `OperacionFinanciera`.

**Entregables del Sprint 2**

- Dominio implementado para operaciones:
  - Lógica de débito/crédito sobre `CuentaBancaria`.
  - Creación de `OperacionFinanciera` y `MovimientoCuenta`.
- Endpoints de operaciones funcionando en entorno local.
- Actualización de la base de datos con tablas de movimientos y operaciones.
- Tests:
  - Pruebas unitarias sobre métodos de dominio.
  - Pruebas de integración básicas sobre el flujo de una transferencia.

**Criterios de aceptación**

- Se puede realizar:
  - Un depósito válido.
  - Una extracción válida.
  - Una transferencia válida entre dos cuentas.
- No se permite:
  - Extracción con saldo insuficiente (debe devolver error coherente).
  - Transferencia con cuenta origen = cuenta destino.
- El saldo actualizado coincide con la suma de movimientos en los casos probados.

---

### 3.3 Sprint 3 — Auditoría, robustez y despliegue

**Objetivo principal**

Agregar **auditoría, fortalecimiento del sistema y despliegue educativo** del MVP.

**Módulos y alcance**

- Auditoría:
  - `EventoAuditoria`
  - Registro de:
    - Logins exitosos/fallidos (si hay login implementado).
    - Operaciones financieras.
- Endpoints adicionales (opcional según tiempo):
  - GET `/api/auditoria/eventos`
- Testing:
  - Ampliar pruebas de integración.
  - Primeros tests de endpoints (E2E ligeros).
- Despliegue:
  - Script o guía para ejecutar en un entorno de “demo” (por ejemplo, algún servidor simple o Docker local).

**Entregables del Sprint 3**

- Entidad `EventoAuditoria` operativa y persistida.
- Registro automático de eventos básicos:
  - Al menos para operaciones financieras.
- Tests:
  - Conjunto mínimo de pruebas que cubra:
    - Casos felices.
    - Casos de error importantes (saldo insuficiente, cuentas bloqueadas, etc.).
- Despliegue:
  - Guía o script para levantar el sistema en modo “demo”.
  - Opcional: archivo Dockerfile o configuración de empaquetado.

**Criterios de aceptación**

- Cada operación financiera generada desde la API crea al menos un `EventoAuditoria`.
- Hay un conjunto de tests que se ejecutan con un solo comando (por ejemplo, `mvn test`).
- El sistema puede ser levantado en un entorno limpio siguiendo los pasos documentados.

---

## 4. Estrategia de Testing

### 4.1 Enfoque: pirámide de pruebas

Se sugiere seguir una **pirámide de testing**:

- Base: **Tests unitarios** (dominio).
- Medio: **Tests de integración** (repositorios, servicios de aplicación).
- Cima: **Tests de API / E2E ligeros**.

### 4.2 Tipos de pruebas

Tabla resumen:

| Tipo de prueba      | Capa principal    | Objetivo                                     | Ejemplos                                                         |
|---------------------|-------------------|----------------------------------------------|------------------------------------------------------------------|
| Unitarias           | Dominio           | Validar reglas de negocio en aislamiento     | `CuentaBancaria.registrarDebito`, `crearTransferencia`          |
| Integración         | Aplicación/Infra | Validar interacción con BD y repositorios    | `OperacionAppService.registrarTransferencia` con BD en memoria  |
| API / E2E ligeras   | Presentación      | Validar endpoints end-to-end simples         | POST `/api/operaciones/depositos` y ver cambio de saldo         |

### 4.3 Distribución por sprint

- **Sprint 1**
  - Tests unitarios de creación de entidades básicas (`Cliente`, `CuentaBancaria`).
  - Test de integración simple para repositorio de `Cliente` y `CuentaBancaria`.

- **Sprint 2**
  - Tests unitarios:
    - Lógica de débito/crédito.
    - Creación de `OperacionFinanciera` y `MovimientoCuenta`.
  - Tests de integración:
    - Flujo completo de una transferencia (con BD).
  - Primer test de API:
    - Llamar a `/api/operaciones/depositos` y verificar saldo.

- **Sprint 3**
  - Extender tests de API:
    - Depósito, extracción, transferencia, casos de error.
  - Tests de auditoría:
    - Verificar que se crea un `EventoAuditoria` para operaciones.
  - Opcional:
    - Pequeños tests de carga manuales (ejecutar muchas operaciones en un loop para ver estabilidad).

### 4.4 Criterios mínimos de calidad

- No se hace merge a `main` si los tests automáticos **no pasan**.
- Al menos:
  - Sprint 1:
    - Cobertura básica en entidades y repositorios clave.
  - Sprint 2:
    - Tests que detecten errores de negocio obvios (saldo insuficiente).
  - Sprint 3:
    - Cobertura de los flujos principales de negocio de punta a punta.

---

## 5. Estrategia de Despliegue

### 5.1 Entornos

Para el MVP educativo, se plantean dos niveles:

1. **Entorno local de desarrollo**
   - Donde se desarrolla y prueba todo inicialmente.
   - BD local (por ejemplo, MySQL o H2 en memoria).

2. **Entorno de demo / “producción educativa”**
   - Una instalación simple donde el sistema corre como si fuera “producción”.
   - Puede ser:
     - Un servidor local.
     - Una VM.
     - Un contenedor Docker.

### 5.2 Flujo de ramas (Git)

Propuesta sencilla:

- Rama `main`:
  - Siempre en estado estable.
  - Contiene las versiones “entregables” (por sprint).

- Rama `develop` (opcional para el MVP):
  - Integración continua de cambios antes de pasar a `main`.

- Ramas de feature:
  - `feature/mod-cta-apertura-cuentas`
  - `feature/mod-op-transferencias`
  - etc.

Flujo típico:

1. Crear rama de feature desde `main` o `develop`.
2. Desarrollar, agregar tests.
3. Hacer PR/MR (aunque seas 1 sola persona, sirve para revisar).
4. Hacer merge cuando los tests pasan.

### 5.3 Pipeline de build y tests

Incluso sin CI/CD en la nube, se define un **pipeline conceptual**:

1. `Build`:
   - Compilar el proyecto.
2. `Test`:
   - Ejecutar tests unitarios e integración.
3. `Package`:
   - Generar el artefacto ejecutable (por ejemplo, `.jar` de Spring Boot).
4. `Deploy`:
   - Levantar el `.jar` en el entorno de demo.

En una etapa posterior se podría automatizar con GitHub Actions u otra herramienta, pero
el documento deja claro el flujo que se espera seguir.

### 5.4 Despliegue manual (MVP)

Pasos típicos para desplegar en entorno de demo:

1. Clonar el repo en el servidor de demo.
2. Configurar variables de entorno o archivo `application.properties` para la BD de demo.
3. Ejecutar:
   
   - Compilación y tests.
   - Empaquetado del jar.
   - Ejecución del jar (por ejemplo, `java -jar sistema-bancario-educativo.jar`).

4. Verificar:
   - Logs de arranque sin errores.
   - Acceso a endpoints básicos (healthcheck, consulta de cliente/cuenta).

---

## 6. Resumen y próximos pasos

Este plan de sprints, testing y despliegue busca:

- **Dividir el trabajo** en iteraciones claras (S1, S2, S3).
- **Garantizar calidad** mínima a través de pruebas unitarias, de integración y de API.
- **Asegurar que el sistema se puede ejecutar** en un entorno realista (demo).

Próximos pasos recomendados:

1. Crear **issues en GitHub** por sprint y por módulo:
   - Ejemplo: `S1 - Implementar entidades Cliente y CuentaBancaria`.
   - Ejemplo: `S2 - Endpoint POST /api/operaciones/transferencias`.
2. Asociar cada issue a:
   - Sprint (milestone).
   - Módulo (label).
3. A medida que avances, ir marcando:
   - Qué está hecho.
   - Qué está testeado.
   - Qué está desplegado en el entorno de demo.

Este documento cierra la **primera fase de diseño** del Sistema Bancario Educativo:
a partir de aquí, todo está listo para pasar a la **fase de implementación iterativa** guiada
por estos sprints.
