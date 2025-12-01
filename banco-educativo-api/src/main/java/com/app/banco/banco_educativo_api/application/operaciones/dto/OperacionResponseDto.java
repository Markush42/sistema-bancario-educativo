package com.app.banco.banco_educativo_api.application.operaciones.dto;

import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoOperacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida para una operación financiera.
 * 
 * Capa: APPLICATION
 */
public record OperacionResponseDto(
        Long operacionId,
        TipoOperacion tipoOperacion,
        Long cuentaOrigenId,
        Long cuentaDestinoId,
        BigDecimal montoTotal,
        LocalDateTime fechaHora,
        String descripcion,
        List<MovimientoResponseDto> movimientosGenerados) {
}
