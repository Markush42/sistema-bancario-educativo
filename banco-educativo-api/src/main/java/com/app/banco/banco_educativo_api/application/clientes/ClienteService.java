package com.app.banco.banco_educativo_api.application.clientes;

import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.exceptions.DocumentoDuplicadoException;
import com.app.banco.banco_educativo_api.application.clientes.mapper.ClienteMapper;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.clientes.TipoDocumento;
import com.app.banco.banco_educativo_api.infrastructure.persistence.clientes.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de aplicación para operaciones de Clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    // 🔹 Inyección por constructor (Spring lo usa automáticamente)
    public ClienteService(ClienteRepository clienteRepository,
                          ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Crear un nuevo cliente a partir del DTO de request.
     */
    @Transactional
    public ClienteResponseDto crearCliente(ClienteRequestDto requestDto) {
          
        // 1) Datos clave para la unicidad
        String numeroDocumento = requestDto.getDni();
        TipoDocumento tipoDocumento = TipoDocumento.DNI; // MVP

        // 2) Pre-chequeo antes de tocar la BD
        boolean existe = clienteRepository
                .existsByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);

        if (existe) {
            throw new DocumentoDuplicadoException(
                    "Ya existe un cliente con documento " +
                    tipoDocumento + " " + numeroDocumento
            );
        }
        Cliente cliente = clienteMapper.toEntity(requestDto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDto(guardado);
    }

    /**
     * Obtener un cliente por ID.
     */
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id " + id));
        return clienteMapper.toResponseDto(cliente);
    }


    /**
     * Listar todos los clientes.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listarTodosLosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toResponseDtoList(clientes);
    }

    /**
     * Eliminar un cliente por ID.
     */
    @Transactional
    public void eliminarClientePorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException("Cliente no encontrado con id " + id);
        }
        clienteRepository.deleteById(id);
    }
}
