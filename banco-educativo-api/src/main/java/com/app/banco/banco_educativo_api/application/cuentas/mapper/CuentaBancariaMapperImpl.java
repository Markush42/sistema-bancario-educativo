package com.app.banco.banco_educativo_api.application.cuentas.mapper;

import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;

import lombok.Data;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementación manual del mapper de CuentaBancaria.
 */
@Component
@Data
public class CuentaBancariaMapperImpl implements CuentaBancariaMapper {

    @Override
    public CuentaBancaria toEntity(CuentaBancariaRequestDto dto) {
        if (dto == null) {
            return null;
        }
        CuentaBancaria cuenta = new CuentaBancaria();

        // Mapear campos del DTO
        cuenta.setSaldoActual(dto.getSaldo());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setMoneda(dto.getMoneda());

        // Generar número de cuenta único (simplificado para MVP)
        // Formato: 6 dígitos aleatorios + "-" + timestamp corto
        String numeroCuenta = String.format("%06d-%d",
                (int) (Math.random() * 1000000),
                System.currentTimeMillis() % 1000000);
        cuenta.setNumeroCuenta(numeroCuenta);

        // El cliente se asigna en el service (porque hay que buscarlo en el repo)
        return cuenta;
    }

    @Override
    public CuentaBancariaResponseDto toResponseDto(CuentaBancaria entity) {
        if (entity == null) {
            return null;
        }
        Long clienteId = null;
        if (entity.getCliente() != null) {
            clienteId = entity.getCliente().getId();
        }
        return new CuentaBancariaResponseDto(
                entity.getId(),
                entity.getSaldoActual(),
                clienteId);
    }

    @Override
    public List<CuentaBancariaResponseDto> toResponseDtoList(List<CuentaBancaria> entities) {
        if (entities == null) {
            return null;
        }
        List<CuentaBancariaResponseDto> list = new ArrayList<>(entities.size());
        for (CuentaBancaria c : entities) {
            list.add(toResponseDto(c));
        }
        return list;
    }

}
