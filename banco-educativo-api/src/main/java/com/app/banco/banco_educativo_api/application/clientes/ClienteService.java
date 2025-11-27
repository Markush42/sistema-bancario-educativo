package com.app.banco.banco_educativo_api.application.clientes;

import com.app.banco.banco_educativo_api.application.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.mapper.ClienteMapper;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.infrastructure.persistence.clientes.ClienteRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDto crearCliente(ClienteRequestDto requestDto) {
        // Mapear DTO a entidad y guardar
        Cliente entidad = clienteMapper.toEntity(requestDto);
        Cliente entidadGuardada = clienteRepository.save(entidad);
        // Mapear entidad guardada a DTO de respuesta
        return clienteMapper.toResponseDto(entidadGuardada);
    }

    public ClienteResponseDto obtenerClientePorId(Long id) {
        Cliente entidad = clienteRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id " + id));
        return clienteMapper.toResponseDto(entidad);
    }

    public List<ClienteResponseDto> listarTodosLosClientes() {
        List<Cliente> lista = clienteRepository.findAll();
        return clienteMapper.toResponseDtoList(lista);
    }

    public void eliminarClientePorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException("Cliente no encontrado con id " + id);
        }
        clienteRepository.deleteById(id);
    }
}
