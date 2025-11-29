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
    'Lucía',
    'Martínez',
    'DNI',
    '32123456',
    'lucia.martinez@example.com',
    '1166112233',
    'Belgrano 850, Mendoza',
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
    'Santiago',
    'Lopez',
    'DNI',
    '33876543',
    'santiago.lopez@example.com',
    '1177223344',
    'Rivadavia 1020, La Plata',
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
    'Carolina',
    'Ramírez',
    'DNI',
    '30555666',
    'carolina.ramirez@example.com',
    '1188334455',
    'Mitre 230, Mar del Plata',
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
    'Federico',
    'Núñez',
    'DNI',
    '29333444',
    'federico.nunez@example.com',
    '1199445566',
    'Av. Colón 1500, Córdoba',
    'BLOQUEADO',
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
    'Valentina',
    'Suárez',
    'DNI',
    '31222111',
    'valentina.suarez@example.com',
    '1122556677',
    'Boulevard Oroño 600, Rosario',
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
    'Bruno',
    'Castro',
    'DNI',
    '28770099',
    'bruno.castro@example.com',
    '1133667788',
    'Rondeau 320, San Miguel de Tucumán',
    'BLOQUEADO',
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
    'Julieta',
    'Domínguez',
    'DNI',
    '30448999',
    'julieta.dominguez@example.com',
    '1144778899',
    'Av. Alem 450, Bahía Blanca',
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
    'Finanzas del Plata',
    'SA',
    'CUIT',
    '30-71543210-5',
    'contacto@finanzasdelplata.com',
    '1133004400',
    'Puerto Madero, Dique 1, Ciudad de Buenos Aires',
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
    'AgroPampeana',
    'SRL',
    'CUIT',
    '30-70112233-7',
    'info@agropampeana.com',
    '1144005500',
    'Ruta 5 km 320, Santa Rosa, La Pampa',
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
    'Servicios Andinos',
    'SA',
    'CUIT',
    '30-70997766-1',
    'administracion@serviciosandinos.com',
    '1155006600',
    'Av. Las Heras 900, Mendoza',
    'BLOQUEADO',
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
    'Matías',
    'Ferreyra',
    'DNI',
    '32765432',
    'matias.ferreyra@example.com',
    '1160123456',
    'Av. San Martín 980, Salta',
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
    'Rocío',
    'Benítez',
    'DNI',
    '31889977',
    'rocio.benitez@example.com',
    '1160789043',
    'Av. Roca 120, Posadas',
    'BLOQUEADO',
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
    'Nahuel',
    'Giménez',
    'DNI',
    '30223344',
    'nahuel.gimenez@example.com',
    '1160456723',
    'French 340, San Juan',
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
    'Agustina',
    'Vega',
    'DNI',
    '29665544',
    'agustina.vega@example.com',
    '1133456789',
    'Chacabuco 720, Neuquén',
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
    'Tomás',
    'Ponce',
    'DNI',
    '29112233',
    'tomas.ponce@example.com',
    '1145012300',
    'Av. Las Heras 430, San Rafael',
    'BLOQUEADO',
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
    'Camila',
    'Ledesma',
    'DNI',
    '30998877',
    'camila.ledesma@example.com',
    '1145897600',
    'Italia 560, Trelew',
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
    'Patagonia Retail',
    'SA',
    'CUIT',
    '30-71654321-9',
    'contacto@patagoniaretail.com',
    '1123456701',
    'Parque Comercial Sur, Ushuaia',
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
    'AndesTech',
    'SRL',
    'CUIT',
    '30-70334455-2',
    'info@andestech.com',
    '1132109876',
    'Av. Principal 1500, San Carlos de Bariloche',
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
    'Campos del Sur',
    'SA',
    'CUIT',
    '30-70881122-4',
    'administracion@camposdelsur.com',
    '1145789012',
    'Ruta 3 km 280, Azul, Buenos Aires',
    'BLOQUEADO',
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
    'Logística Norte',
    'SRL',
    'CUIT',
    '30-70229876-6',
    'contacto@logisticanorte.com',
    '1156789043',
    'Parque Industrial Norte, Tucumán',
    'ACTIVO',
    CURRENT_TIMESTAMP
);
