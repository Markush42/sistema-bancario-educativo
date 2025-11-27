package com.app.banco.banco_educativo_api.application.mapper;


import com.app.banco.banco_educativo_api.application.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuentaBancariaMapper {
    @Mapping(target = "id", ignore = true)                // el ID se genera en la creación
    @Mapping(target = "cliente", ignore = true)          // se asignará manualmente en el servicio
    CuentaBancaria toEntity(CuentaBancariaRequestDto dto);

    @Mapping(source = "cliente.id", target = "clienteId") // mapea el ID del cliente asociado
    CuentaBancariaResponseDto toResponseDto(CuentaBancaria entity);

    List<CuentaBancariaResponseDto> toResponseDtoList(List<CuentaBancaria> entities);
}
