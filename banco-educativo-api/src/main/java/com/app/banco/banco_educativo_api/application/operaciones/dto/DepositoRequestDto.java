package com.app.banco.banco_educativo_api.application.operaciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO de entrada para registrar un depósito.
 * 
 * Capa: APPLICATION
 */
public record DepositoRequestDto(
        @NotNull(message = "La cuenta destino es obligatoria") Long cuentaDestinoId,

        @NotNull(message = "El monto es obligatorio") @Positive(message = "El monto debe ser positivo") BigDecimal monto,

        String descripcion) {
}
