package com.app.banco.banco_educativo_api.application.clientes;

import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateRequestDto;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteUpdateResponseDto;
import com.app.banco.banco_educativo_api.application.clientes.exceptions.DocumentoDuplicadoException;
import com.app.banco.banco_educativo_api.application.clientes.mapper.ClienteMapper;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoDocumento;
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

@Transactional
public ClienteUpdateResponseDto actualizarCliente(Long id, ClienteUpdateRequestDto requestDto) {

    // 1) Busco el cliente existente
    Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                    "Cliente no encontrado con id " + id
            ));

    // 2) Datos nuevos de documento que vienen en el DTO
    String nuevoNumeroDocumento = requestDto.numeroDocumento();
    TipoDocumento nuevoTipoDocumento = requestDto.tipoDocumento();

    // 3) Verifico si realmente cambió el documento
    boolean cambioDocumento =
            !cliente.getNumeroDocumento().equals(nuevoNumeroDocumento) ||
            cliente.getTipoDocumento() != nuevoTipoDocumento;

    // 4) Si cambió, valido unicidad
    if (cambioDocumento) {
        boolean existe = clienteRepository
                .existsByTipoDocumentoAndNumeroDocumento(nuevoTipoDocumento, nuevoNumeroDocumento);

        if (existe) {
            throw new DocumentoDuplicadoException(
                    "Ya existe un cliente con documento " +
                    nuevoTipoDocumento + " " + nuevoNumeroDocumento
            );
        }
    }

    // 5) Actualizo campos "simples" usando el mapper (sin tocar documento acá)
    clienteMapper.updateEntityFromDto(requestDto, cliente);

    // 6) Documento y tipoDocumento se actualizan explícitamente
    cliente.setTipoDocumento(nuevoTipoDocumento);
    cliente.setNumeroDocumento(nuevoNumeroDocumento);

    // 7) Persisto cambios
    Cliente actualizado = clienteRepository.save(cliente);

    // 8) Devuelvo DTO de respuesta
    return clienteMapper.toResponseUpdateDto(actualizado);
}

}
