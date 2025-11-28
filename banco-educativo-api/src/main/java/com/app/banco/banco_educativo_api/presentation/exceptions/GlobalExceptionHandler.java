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
            ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
}


    // Fallback genérico por si alguna constraint de BD se escapa
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Violación de integridad de datos",
                "message", "Los datos enviados violan una restricción de unicidad o integridad."
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
