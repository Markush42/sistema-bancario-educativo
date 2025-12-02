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

/**
 * Controlador REST para operaciones sobre Clientes.
 *
 * Capa: PRESENTATION
 * Depende de: application.clientes.ClienteService
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteRestController {

    private final ClienteService clienteService;

    /**
     * Alta de cliente.
     *
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDto> crearCliente(
            @Valid @RequestBody ClienteRequestDto requestDto) {
        ClienteResponseDto response = clienteService.crearCliente(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequestDto requestDto) {
        ClienteResponseDto response = clienteService.actualizarCliente(id, requestDto);
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
     * Bloquear cliente.
     * 
     * POST /api/clientes/{id}/bloquear
     */
    @PostMapping("/{id}/bloquear")
    public ResponseEntity<BloqueoClienteResponseDto> bloquearCliente(@PathVariable Long id) {
        BloqueoClienteResponseDto response = clienteService.bloquearCliente(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Activar cliente (desbloquear).
     * 
     * POST /api/clientes/{id}/activar
     */
    @PostMapping("/{id}/activar")
    public ResponseEntity<BloqueoClienteResponseDto> activarCliente(@PathVariable Long id) {
        BloqueoClienteResponseDto response = clienteService.activarCliente(id);
        return ResponseEntity.ok(response);
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

    /**
     * Listar clientes paginados.
     *
     * GET /api/clientes
     */
    @GetMapping
    public Page<ClienteResponseDto> listarClientes(Pageable pageable) {
        return clienteService.listarClientes(pageable);
    }
}
