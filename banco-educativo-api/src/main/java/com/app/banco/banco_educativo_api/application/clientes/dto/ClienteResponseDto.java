package com.app.banco.banco_educativo_api.application.clientes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para devolver datos de Cliente.
 */
@Data
@NoArgsConstructor
public class ClienteResponseDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
}
