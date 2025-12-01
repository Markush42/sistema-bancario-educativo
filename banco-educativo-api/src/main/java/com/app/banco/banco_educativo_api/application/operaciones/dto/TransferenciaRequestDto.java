package com.app.banco.banco_educativo_api.application.operaciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO de entrada para registrar una transferencia interna.
 * 
 * Capa: APPLICATION
 */
public record TransferenciaRequestDto(
        @NotNull(message = "La cuenta origen es obligatoria") Long cuentaOrigenId,

        @NotNull(message = "La cuenta destino es obligatoria") Long cuentaDestinoId,

        @NotNull(message = "El monto es obligatorio") @Positive(message = "El monto debe ser positivo") BigDecimal monto,

        String descripcion) {
}
