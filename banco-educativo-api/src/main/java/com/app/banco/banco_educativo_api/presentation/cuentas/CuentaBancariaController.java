package com.app.banco.banco_educativo_api.presentation.cuentas;

import com.app.banco.banco_educativo_api.application.cuentas.CuentaBancariaService;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para operaciones sobre Cuentas Bancarias.
 *
 * Capa: PRESENTATION
 * Depende de: application.cuentas.CuentaBancariaService
 */
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaBancariaController {

    private final CuentaBancariaService cuentaBancariaService;

    /**
     * Apertura de cuenta bancaria.
     *
     * POST /api/cuentas
     */
    @PostMapping
    public ResponseEntity<CuentaBancariaResponseDto> crearCuenta(
            @Valid @RequestBody CuentaBancariaRequestDto requestDto
    ) {
        CuentaBancariaResponseDto response = cuentaBancariaService.crearCuentaBancaria(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener datos completos de una cuenta por ID.
     *
     * GET /api/cuentas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuentaBancariaResponseDto> obtenerCuenta(@PathVariable Long id) {
        CuentaBancariaResponseDto response = cuentaBancariaService.obtenerCuentaBancariaPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar todas las cuentas.
     *
     * GET /api/cuentas
     */
    @GetMapping
    public ResponseEntity<List<CuentaBancariaResponseDto>> listarCuentas() {
        List<CuentaBancariaResponseDto> cuentas = cuentaBancariaService.listarTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }

    /**
     * Consultar saldo de una cuenta.
     *
     * GET /api/cuentas/{id}/saldo
     *
     * Para el MVP devolvemos un JSON simple con el saldo.
     */
    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> obtenerSaldo(@PathVariable Long id) {
        CuentaBancariaResponseDto cuenta = cuentaBancariaService.obtenerCuentaBancariaPorId(id);

        // Podrías devolver directamente la cuenta, pero acá hago un payload bien explícito:
        return ResponseEntity.ok(
                new SaldoResponse(
                        cuenta.getId(),
                        cuenta.getSaldo()
                )
        );
    }

    /**
     * Eliminar cuenta por ID.
     *
     * DELETE /api/cuentas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        cuentaBancariaService.eliminarCuentaBancariaPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DTO interno para respuesta de saldo.
     * Si querés, lo podés mover a un paquete dto específico.
     */
    public record SaldoResponse(Long cuentaId, BigDecimal saldoActual) {}
}
