package com.app.banco.banco_educativo_api.application.clientes.mapper;

import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import java.util.List;

import org.springframework.data.domain.Page;

public interface ClienteMapper { // el ID se genera automáticamente
    Cliente toEntity(ClienteRequestDto dto);

    ClienteResponseDto toResponseDto(Cliente entity);

    List<ClienteResponseDto> toResponseDtoList(List<Cliente> entities);

    void updateEntityFromDto(ClienteUpdateRequestDto dto, Cliente entity);

    List<ClienteResponseDto> toResponseUpdateDtoList(List<Cliente> entities);

    Page<ClienteResponseDto> toResponseDtoPage(Page<Cliente> page);
}
