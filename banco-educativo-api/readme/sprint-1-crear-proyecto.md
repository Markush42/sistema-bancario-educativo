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

- [ ] Crear el proyecto backend (por ejemplo, usando Spring Initializr) con:
  - [ ] Java (versión definida para el proyecto).  
  - [ ] Maven como herramienta de build.  
  - [ ] Dependencias mínimas:
    - [ ] `spring-boot-starter-web`
    - [ ] (Opcional) `spring-boot-starter-data-jpa` si se quiere dejar preconfigurado.

- [ ] Definir el **paquete base** del proyecto (ejemplo: `com.bancoeducativo`).

- [ ] Crear los paquetes por **capas** en el código fuente:

  - [ ] `com.bancoeducativo.presentation`  
  - [ ] `com.bancoeducativo.application`  
  - [ ] `com.bancoeducativo.domain`  
  - [ ] `com.bancoeducativo.infrastructure`  

- [ ] Crear subpaquetes por **módulos funcionales** donde aplique:

  - [ ] `com.bancoeducativo.presentation.empleados`  
  - [ ] `com.bancoeducativo.presentation.clientes`  
  - [ ] `com.bancoeducativo.presentation.cuentas`  
  - [ ] `com.bancoeducativo.presentation.operaciones`  

  - [ ] `com.bancoeducativo.application.empleados`  
  - [ ] `com.bancoeducativo.application.clientes`  
  - [ ] `com.bancoeducativo.application.cuentas`  
  - [ ] `com.bancoeducativo.application.operaciones`  

  - [ ] `com.bancoeducativo.domain.empleados`  
  - [ ] `com.bancoeducativo.domain.clientes`  
  - [ ] `com.bancoeducativo.domain.cuentas`  
  - [ ] `com.bancoeducativo.domain.operaciones`  
  - [ ] `com.bancoeducativo.domain.auditoria`  

  - [ ] `com.bancoeducativo.infrastructure.persistence`  
  - [ ] `com.bancoeducativo.infrastructure.config`  

- [ ] Añadir clases vacías / marcadores mínimos para cada capa (solo para que no queden paquetes vacíos):

  - [ ] `com.bancoeducativo.presentation.HealthCheckController`  
  - [ ] `com.bancoeducativo.application.ApplicationMarker`  
  - [ ] `com.bancoeducativo.domain.DomainMarker`  
  - [ ] `com.bancoeducativo.infrastructure.InfrastructureMarker`  

- [ ] Implementar un **endpoint mínimo de health-check**:

  - [ ] `GET /api/health` que devuelva un JSON sencillo, por ejemplo:

        {
          "status": "UP",
          "app": "sistema-bancario-educativo"
        }

- [ ] Verificar que la aplicación se ejecuta (por ejemplo, con `mvn spring-boot:run`) y que el endpoint `/api/health` responde `200 OK`.

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
