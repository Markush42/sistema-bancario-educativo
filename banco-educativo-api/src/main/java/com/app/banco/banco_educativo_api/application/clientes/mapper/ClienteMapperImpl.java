package com.app.banco.banco_educativo_api.application.clientes.mapper;

import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateRequestDto;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación manual de ClienteMapper.
 * Se encarga de convertir entre DTOs y la entidad Cliente.
 */
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toEntity(ClienteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();

        // Mapear todos los campos del DTO
        cliente.setTipoPersona(dto.tipoPersona());
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setTipoDocumento(dto.tipoDocumento());
        cliente.setNumeroDocumento(dto.numeroDocumento());
        cliente.setEmail(dto.email());
        cliente.setTelefono(dto.telefono());
        cliente.setDireccion(dto.direccion());
        cliente.setEstado(dto.estado());

        return cliente;
    }

    @Override
    public void updateEntityFromDto(ClienteUpdateRequestDto dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Solo actualizo si viene un valor no null en el DTO
        if (dto.tipoPersona() != null) {
            entity.setTipoPersona(dto.tipoPersona());
        }
        if (dto.nombre() != null) {
            entity.setNombre(dto.nombre());
        }
        if (dto.apellido() != null) {
            entity.setApellido(dto.apellido());
        }
        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }
        if (dto.telefono() != null) {
            entity.setTelefono(dto.telefono());
        }
        if (dto.direccion() != null) {
            entity.setDireccion(dto.direccion());
        }
        if (dto.estado() != null) {
            entity.setEstado(dto.estado());
        }

    }

    @Override
    public ClienteResponseDto toResponseDto(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteResponseDto dto = new ClienteResponseDto(
                entity.getId(),
                entity.getTipoPersona(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getTipoDocumento(),
                entity.getNumeroDocumento(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getEstado());

        return dto;
    }

    @Override
    public List<ClienteResponseDto> toResponseDtoList(List<Cliente> entities) {
        if (entities == null) {
            return null;
        }
        List<ClienteResponseDto> list = new ArrayList<>(entities.size());
        for (Cliente c : entities) {
            list.add(toResponseDto(c));
        }
        return list;
    }

    @Override
    public List<ClienteResponseDto> toResponseUpdateDtoList(List<Cliente> entities) {
        if (entities == null) {
            return null;
        }
        List<ClienteResponseDto> list = new ArrayList<>(entities.size());
        for (Cliente c : entities) {
            list.add(toResponseDto(c));
        }
        return list;
    }

    public Page<ClienteResponseDto> toResponseDtoPage(Page<Cliente> page) {
        if (page == null) {
            return Page.empty();
        }
        return page.map(this::toResponseDto);
    }

}
