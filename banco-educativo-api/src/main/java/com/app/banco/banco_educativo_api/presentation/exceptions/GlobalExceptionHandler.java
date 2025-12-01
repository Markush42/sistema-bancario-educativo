package com.app.banco.banco_educativo_api.presentation.exceptions;

import com.app.banco.banco_educativo_api.application.clientes.exceptions.DocumentoDuplicadoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDocumentoDuplicado(DocumentoDuplicadoException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Documento duplicado",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.app.banco.banco_educativo_api.application.operaciones.exceptions.SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> manejarSaldoInsuficiente(
            com.app.banco.banco_educativo_api.application.operaciones.exceptions.SaldoInsuficienteException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(), // 409 Conflict es apropiado para saldo insuficiente
                "Saldo insuficiente",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(com.app.banco.banco_educativo_api.application.operaciones.exceptions.OperacionInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarOperacionInvalida(
            com.app.banco.banco_educativo_api.application.operaciones.exceptions.OperacionInvalidaException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // 400 Bad Request para reglas de negocio inválidas
                "Operación inválida",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Fallback genérico por si alguna constraint de BD se escapa
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Violación de integridad de datos",
                "message", "Los datos enviados violan una restricción de unicidad o integridad.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
