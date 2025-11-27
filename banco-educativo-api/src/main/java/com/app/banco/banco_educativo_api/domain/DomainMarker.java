package com.app.banco.banco_educativo_api.domain;

/**
 * Clase marcador (marker) para la capa de dominio.
 *
 * Sirve como punto de anclaje para:
 * - Referenciar el paquete de dominio en configuraciones.
 * - Ayudar a separar responsabilidad de la lógica de negocio.
 */
public final class DomainMarker {

    private DomainMarker() {
        throw new UnsupportedOperationException("DomainMarker no debe ser instanciado");
    }
}
