package com.app.banco.banco_educativo_api.application.cuentas.mapper;

import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;

import java.util.List;

public interface CuentaBancariaMapper {

    CuentaBancaria toEntity(CuentaBancariaRequestDto dto);

    CuentaBancariaResponseDto toResponseDto(CuentaBancaria entity);

    List<CuentaBancariaResponseDto> toResponseDtoList(List<CuentaBancaria> entities);
}
