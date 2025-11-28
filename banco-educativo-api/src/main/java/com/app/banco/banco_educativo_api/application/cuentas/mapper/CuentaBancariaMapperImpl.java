package com.app.banco.banco_educativo_api.application.cuentas.mapper;

import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;

import lombok.Data;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        // asumimos que la entidad tiene setSaldoActual(BigDecimal)
        cuenta.setSaldoActual(dto.getSaldo());
        // el cliente se asigna en el service (porque hay que buscarlo en el repo)
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
                clienteId
        );
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
