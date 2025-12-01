package com.app.banco.banco_educativo_api.application.clientes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para crear un nuevo Cliente.
 * 
 */
public record ClienteRequestDto(
                @NotNull @Size(min = 1, max = 50) String nombre,
                @NotNull @Size(min = 1, max = 50) String apellido,
                @NotNull @Size(min = 7, max = 8) String dni) {
}
