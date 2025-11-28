package com.app.banco.banco_educativo_api.application.clientes.dto;

/**
 * DTO de salida para devolver datos de Cliente.
 * Implementado como record (28/11/25).
 */
public record ClienteResponseDto(
        Long id,
        String nombre,
        String apellido,
        String dni
) {
}
