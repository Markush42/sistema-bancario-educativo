package com.app.banco.banco_educativo_api.application.operaciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO de entrada para registrar una extracción.
 * 
 * Capa: APPLICATION
 */
public record ExtraccionRequestDto(
        @NotNull(message = "La cuenta origen es obligatoria") Long cuentaOrigenId,

        @NotNull(message = "El monto es obligatorio") @Positive(message = "El monto debe ser positivo") BigDecimal monto,

        String descripcion) {
}
