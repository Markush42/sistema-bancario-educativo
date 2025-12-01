package com.app.banco.banco_educativo_api.presentation.operaciones;

import com.app.banco.banco_educativo_api.application.operaciones.OperacionFinancieraService;
import com.app.banco.banco_educativo_api.application.operaciones.dto.DepositoRequestDto;
import com.app.banco.banco_educativo_api.application.operaciones.dto.ExtraccionRequestDto;
import com.app.banco.banco_educativo_api.application.operaciones.dto.OperacionResponseDto;
import com.app.banco.banco_educativo_api.application.operaciones.dto.TransferenciaRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones financieras.
 * 
 * Capa: PRESENTATION
 * Depende de: application.operaciones.OperacionFinancieraService
 */
@RestController
@RequestMapping("/api/operaciones")
@RequiredArgsConstructor
public class OperacionFinancieraController {

    private final OperacionFinancieraService operacionFinancieraService;

    /**
     * Registrar depósito.
     * 
     * POST /api/operaciones/depositos
     */
    @PostMapping("/depositos")
    public ResponseEntity<OperacionResponseDto> registrarDeposito(
            @Valid @RequestBody DepositoRequestDto requestDto) {
        OperacionResponseDto response = operacionFinancieraService.registrarDeposito(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Registrar extracción.
     * 
     * POST /api/operaciones/extracciones
     */
    @PostMapping("/extracciones")
    public ResponseEntity<OperacionResponseDto> registrarExtraccion(
            @Valid @RequestBody ExtraccionRequestDto requestDto) {
        OperacionResponseDto response = operacionFinancieraService.registrarExtraccion(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Registrar transferencia interna.
     * 
     * POST /api/operaciones/transferencias
     */
    @PostMapping("/transferencias")
    public ResponseEntity<OperacionResponseDto> registrarTransferencia(
            @Valid @RequestBody TransferenciaRequestDto requestDto) {
        OperacionResponseDto response = operacionFinancieraService.registrarTransferencia(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
