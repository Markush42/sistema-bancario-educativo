-- ============================================
-- DATA SEED INICIAL PARA TABLA CLIENTES (H2)
-- Archivo: src/main/resources/data.sql
-- ============================================

-- NOTA:
-- - No seteamos la columna ID (IDENTITY): la genera H2 automáticamente.
-- - CURRENT_TIMESTAMP se evalúa al momento de la inserción.

INSERT INTO clientes (
    tipo_persona,
    nombre,
    apellido,
    tipo_documento,
    numero_documento,
    email,
    telefono,
    direccion,
    estado,
    fecha_alta
) VALUES (
    'FISICA',
    'Marcos',
    'Arevalo',
    'DNI',
    '30113156',
    'marcos.arevalo@example.com',
    '1122334455',
    'Calle Falsa 123, Buenos Aires',
    'ACTIVO',
    CURRENT_TIMESTAMP
);

INSERT INTO clientes (
    tipo_persona,
    nombre,
    apellido,
    tipo_documento,
    numero_documento,
    email,
    telefono,
    direccion,
    estado,
    fecha_alta
) VALUES (
    'FISICA',
    'Ana',
    'González',
    'DNI',
    '28999888',
    'ana.gonzalez@example.com',
    '1133445566',
    'Av. Siempre Viva 742, Buenos Aires',
    'ACTIVO',
    CURRENT_TIMESTAMP
);

INSERT INTO clientes (
    tipo_persona,
    nombre,
    apellido,
    tipo_documento,
    numero_documento,
    email,
    telefono,
    direccion,
    estado,
    fecha_alta
) VALUES (
    'FISICA',
    'Juan',
    'Pérez',
    'DNI',
    '27555111',
    'juan.perez@example.com',
    '1144556677',
    'San Martín 450, Córdoba',
    'ACTIVO',
    CURRENT_TIMESTAMP
);

INSERT INTO clientes (
    tipo_persona,
    nombre,
    apellido,
    tipo_documento,
    numero_documento,
    email,
    telefono,
    direccion,
    estado,
    fecha_alta
) VALUES (
    'JURIDICA',
    'Tech Solutions',
    'SRL',
    'CUIT',
    '30-71234567-8',
    'contacto@techsolutions.com',
    '1166778899',
    'Parque Industrial, Nave 5, Rosario',
    'ACTIVO',
    CURRENT_TIMESTAMP
);

INSERT INTO clientes (
    tipo_persona,
    nombre,
    apellido,
    tipo_documento,
    numero_documento,
    email,
    telefono,
    direccion,
    estado,
    fecha_alta
) VALUES (
    'JURIDICA',
    'LogiTrans',
    'SA',
    'CUIT',
    '30-70998877-3',
    'info@logitrans.com',
    '1199001122',
    'Ruta 8 km 50, Parque Logístico, Buenos Aires',
    'ACTIVO',
    CURRENT_TIMESTAMP
);
