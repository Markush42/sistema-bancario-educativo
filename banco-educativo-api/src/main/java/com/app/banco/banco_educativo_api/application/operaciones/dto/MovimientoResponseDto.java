package com.app.banco.banco_educativo_api.application.operaciones.dto;

import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida para un movimiento de cuenta.
 * 
 * Capa: APPLICATION
 */
public record MovimientoResponseDto(
        Long idMovimiento,
        Long cuentaId,
        TipoMovimiento tipoMovimiento,
        BigDecimal monto,
        BigDecimal saldoPosterior,
        LocalDateTime fechaHora,
        String descripcion) {
}
