package com.app.banco.banco_educativo_api.application.clientes.dto;

/**
 * DTO de salida para la operación de bloqueo de cliente.
 * 
 * Capa: APPLICATION
 */
public record BloqueoClienteResponseDto(
        Long id,
        String estadoAnterior,
        String estadoActual) {
}
