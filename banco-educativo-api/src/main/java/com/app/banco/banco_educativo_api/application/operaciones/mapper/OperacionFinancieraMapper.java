package com.app.banco.banco_educativo_api.application.operaciones.mapper;

import com.app.banco.banco_educativo_api.application.operaciones.dto.MovimientoResponseDto;
import com.app.banco.banco_educativo_api.application.operaciones.dto.OperacionResponseDto;
import com.app.banco.banco_educativo_api.domain.operaciones.MovimientoCuenta;
import com.app.banco.banco_educativo_api.domain.operaciones.OperacionFinanciera;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entidades de operaciones a DTOs.
 * 
 * Capa: APPLICATION
 */
@Component
public class OperacionFinancieraMapper {

    /**
     * Convierte una entidad OperacionFinanciera a DTO de respuesta.
     */
    public OperacionResponseDto toOperacionResponseDto(OperacionFinanciera operacion) {
        List<MovimientoResponseDto> movimientos = operacion.getMovimientos()
                .stream()
                .map(this::toMovimientoResponseDto)
                .collect(Collectors.toList());

        return new OperacionResponseDto(
                operacion.getId(),
                operacion.getTipoOperacion(),
                operacion.getCuentaOrigen() != null ? operacion.getCuentaOrigen().getId() : null,
                operacion.getCuentaDestino() != null ? operacion.getCuentaDestino().getId() : null,
                operacion.getMontoTotal(),
                operacion.getFechaHora(),
                operacion.getDescripcion(),
                movimientos);
    }

    /**
     * Convierte una entidad MovimientoCuenta a DTO de respuesta.
     */
    public MovimientoResponseDto toMovimientoResponseDto(MovimientoCuenta movimiento) {
        return new MovimientoResponseDto(
                movimiento.getId(),
                movimiento.getCuentaBancaria().getId(),
                movimiento.getTipoMovimiento(),
                movimiento.getMonto(),
                movimiento.getSaldoPosterior(),
                movimiento.getFechaHora(),
                movimiento.getDescripcion());
    }

    /**
     * Convierte una lista de movimientos a DTOs.
     */
    public List<MovimientoResponseDto> toMovimientoDtoList(List<MovimientoCuenta> movimientos) {
        return movimientos.stream()
                .map(this::toMovimientoResponseDto)
                .collect(Collectors.toList());
    }
}
