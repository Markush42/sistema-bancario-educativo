package com.app.banco.banco_educativo_api.application.mapper;

import com.app.banco.banco_educativo_api.application.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", ignore = true)  // el ID se genera automáticamente
    Cliente toEntity(ClienteRequestDto dto);

    ClienteResponseDto toResponseDto(Cliente entity);

    List<ClienteResponseDto> toResponseDtoList(List<Cliente> entities);
}
