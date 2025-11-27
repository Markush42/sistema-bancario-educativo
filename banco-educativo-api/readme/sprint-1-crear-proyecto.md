# Sprint 1 — [S1] Crear proyecto backend y estructura de capas

## 1. Descripción

Crear el **proyecto backend** del Sistema Bancario Educativo y definir la **estructura de paquetes por capas**, alineada con la documentación:

- `10-arquitectura-capas.md`  
- `11-modulos-responsabilidades.md`  
- `12-plan-sprints-testing-despliegue.md`  

Esta tarea NO busca implementar lógica de negocio, solo dejar listo el **esqueleto técnico** sobre el que se construirá el MVP.

---

## 2. Objetivo

Disponer de un proyecto backend que:

- Compile y levante correctamente.  
- Tenga una **arquitectura en capas explícita** (`presentation`, `application`, `domain`, `infrastructure`).  
- Esté preparado para empezar a implementar los casos de uso del Sprint 1.

---

## 3. Alcance

### 3.1 Incluye

- Creación del proyecto backend (por ejemplo, Spring Boot + Maven + Java).  
- Definición del **paquete base** (ejemplo: `com.bancoeducativo` o similar).  
- Creación de la **estructura de paquetes** por capas y módulos:

    com.bancoeducativo  
     ├─ presentation        (capa de presentación / API REST)  
     │   ├─ empleados  
     │   ├─ clientes  
     │   ├─ cuentas  
     │   └─ operaciones  
     ├─ application         (servicios de aplicación / casos de uso)  
     │   ├─ empleados  
     │   ├─ clientes  
     │   ├─ cuentas  
     │   └─ operaciones  
     ├─ domain              (modelo de dominio bancario)  
     │   ├─ empleados  
     │   ├─ clientes  
     │   ├─ cuentas  
     │   ├─ operaciones  
     │   └─ auditoria  
     └─ infrastructure      (persistencia, config técnica, adaptadores)  
         ├─ persistence  
         └─ config  

### 3.2 No incluye

- Implementar entidades del dominio.  
- Implementar servicios de aplicación.  
- Implementar controladores ni endpoints de negocio (salvo un health-check mínimo).  
- Configuración detallada de seguridad o auditoría.

---

## 4. Tareas

- [X] Crear el proyecto backend (por ejemplo, usando Spring Initializr) con:
  - [X] Java (versión definida para el proyecto).  
  - [x] Maven como herramienta de build.  
  - [X] Dependencias mínimas:
    - [X] `spring-boot-starter-web`
    - [X] (Opcional) `spring-boot-starter-data-jpa` si se quiere dejar preconfigurado.

- [X] Definir el **paquete base** del proyecto (ejemplo: `com.bancoeducativo`).

- [X] Crear los paquetes por **capas** en el código fuente:

  - [X] `com.bancoeducativo.presentation`  
  - [X] `com.bancoeducativo.application`  
  - [X] `com.bancoeducativo.domain`  
  - [X] `com.bancoeducativo.infrastructure`  

- [X] Crear subpaquetes por **módulos funcionales** donde aplique:

  - [X] `com.bancoeducativo.presentation.empleados`  
  - [X] `com.bancoeducativo.presentation.clientes`  
  - [X] `com.bancoeducativo.presentation.cuentas`  
  - [X] `com.bancoeducativo.presentation.operaciones`  

  - [X] `com.bancoeducativo.application.empleados`  
  - [X] `com.bancoeducativo.application.clientes`  
  - [X] `com.bancoeducativo.application.cuentas`  
  - [X] `com.bancoeducativo.application.operaciones`  

  - [X] `com.bancoeducativo.domain.empleados`  
  - [X] `com.bancoeducativo.domain.clientes`  
  - [X] `com.bancoeducativo.domain.cuentas`  
  - [X] `com.bancoeducativo.domain.operaciones`  
  - [X] `com.bancoeducativo.domain.auditoria`  

  - [X] `com.bancoeducativo.infrastructure.persistence`  
  - [X] `com.bancoeducativo.infrastructure.config`  

- [X] Añadir clases vacías / marcadores mínimos para cada capa (solo para que no queden paquetes vacíos):

  - [X] `com.bancoeducativo.presentation.HealthCheckController`  
  - [X] `com.bancoeducativo.application.ApplicationMarker`  
  - [X] `com.bancoeducativo.domain.DomainMarker`  
  - [X] `com.bancoeducativo.infrastructure.InfrastructureMarker`  

- [X] Implementar un **endpoint mínimo de health-check**:

  - [X] `GET /api/health` que devuelva un JSON sencillo, por ejemplo:

        {
          "status": "UP",
          "app": "sistema-bancario-educativo"
        }

- [X] Verificar que la aplicación se ejecuta (por ejemplo, con `mvn spring-boot:run`) y que el endpoint `/api/health` responde `200 OK`.

---

## 5. Criterios de aceptación (Definition of Done)

- [X] El proyecto backend compila sin errores.  
- [X] La aplicación arranca correctamente en local.  
- [X] Existe un endpoint `GET /api/health` que responde `200 OK` con un cuerpo JSON.  
- [X] La estructura de paquetes respeta la arquitectura definida en `10-arquitectura-capas.md`.  
- [X] Se realizó al menos un commit asociado a esta tarea con un mensaje similar a:

  - `chore: crear proyecto backend y estructura de capas base`

---

## 6. Notas técnicas

- Nombre sugerido del módulo/proyecto:  
  - `sistema-bancario-educativo-api`  
  - o `bank-educativo-api`.  

- El `HealthCheckController` puede ubicarse en:
  - `com.bancoeducativo.presentation`  
  - o en un subpaquete `com.bancoeducativo.presentation.health`.

- Esta tarea prepara el terreno para las siguientes de Sprint 1:
  - `[S1] Modelar entidades Cliente y CuentaBancaria (dominio + JPA)`  
  - `[S1] Implementar repositorios Cliente y CuentaBancaria`  
  - `[S1] Endpoint GET /api/clientes/{id}`
