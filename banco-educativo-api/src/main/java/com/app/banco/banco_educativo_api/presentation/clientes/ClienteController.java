package com.app.banco.banco_educativo_api.presentation.clientes;

import com.app.banco.banco_educativo_api.application.clientes.ClienteService;
import com.app.banco.banco_educativo_api.application.clientes.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones sobre Clientes.
 *
 * Capa: PRESENTATION
 * Depende de: application.clientes.ClienteService
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Alta de cliente.
     *
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDto> crearCliente(
            @Valid @RequestBody ClienteRequestDto requestDto
    ) {

        ClienteResponseDto response = clienteService.crearCliente(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteUpdateResponseDto> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequestDto requestDto
    ) {
        ClienteUpdateResponseDto response = clienteService.actualizarCliente(id, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener cliente por ID.
     *
     * GET /api/clientes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerCliente(@PathVariable Long id) {
        ClienteResponseDto response = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar todos los clientes.
     *
     * GET /api/clientes
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        List<ClienteResponseDto> clientes = clienteService.listarTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Eliminar cliente por ID.
     *
     * DELETE /api/clientes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarClientePorId(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public Page<ClienteResponseDto> listarClientes(Pageable pageable) {
        return clienteService.listarClientes(pageable);
}
}

