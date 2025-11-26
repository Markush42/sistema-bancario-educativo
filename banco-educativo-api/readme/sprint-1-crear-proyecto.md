
### [S1] Crear proyecto backend y estructura de capas

#### 1. Descripción

Crear el **proyecto backend** del Sistema Bancario Educativo y definir la **estructura de paquetes por capas**, alineada con la documentación:

- `10-arquitectura-capas.md`
- `11-modulos-responsabilidades.md`
- `12-plan-sprints-testing-despliegue.md`

Esta tarea NO busca implementar lógica de negocio, solo dejar listo el esqueleto técnico sobre el que se construirá el MVP.

---

#### 2. Objetivo

Disponer de un proyecto backend que:

- Compile y levante correctamente.
- Tenga una **arquitectura en capas explícita** (presentation, application, domain, infrastructure).
- Esté preparado para empezar a implementar los casos de uso del Sprint 1.

---

#### 3. Alcance

Incluye:

- Creación del proyecto backend (ej. Spring Boot + Maven + Java XX).
- Definición del **paquete base** (ej: `com.bancoeducativo` o similar).
- Creación de la **estructura de paquetes** por capas y módulos:

  ```text
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
