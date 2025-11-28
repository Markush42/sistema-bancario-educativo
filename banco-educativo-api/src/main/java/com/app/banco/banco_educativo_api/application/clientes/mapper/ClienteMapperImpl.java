package com.app.banco.banco_educativo_api.application.clientes.mapper;

import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateResponseDto;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoDocumento;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoPersona;

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

        // Campos básicos
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setNumeroDocumento(dto.getDni());

        // 🔹 Valores por defecto para el MVP:
        // Todos los clientes son personas físicas con DNI.
        cliente.setTipoPersona(TipoPersona.FISICA);
        cliente.setTipoDocumento(TipoDocumento.DNI);

        // email, telefono, direccion quedan null (opcionales)
        // estado y fechaAlta se setean en @PrePersist

        return cliente;
    }

    public void updateEntityFromDto(ClienteUpdateRequestDto dto, Cliente entity) {
    entity.setTipoPersona(dto.tipoPersona());
    entity.setNombre(dto.nombre());
    entity.setApellido(dto.apellido());
    entity.setEmail(dto.email());
    entity.setTelefono(dto.telefono());
    entity.setDireccion(dto.direccion());
    entity.setEstado(dto.estado());
    // el documento lo manejás aparte por el tema unicidad
}



    
    @Override
    public ClienteResponseDto toResponseDto(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteResponseDto dto = new ClienteResponseDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDni(entity.getNumeroDocumento());
        return dto;
    }

    @Override
    public ClienteUpdateResponseDto toResponseUpdateDto(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteUpdateResponseDto dto = new ClienteUpdateResponseDto(
            entity.getId(),
            entity.getTipoPersona(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getTipoDocumento(),
            entity.getNumeroDocumento(),
            entity.getEmail(),
            entity.getTelefono(),
            entity.getDireccion(),
            entity.getEstado()
        );
        
        //System.out.println(dto.);

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

    
}
