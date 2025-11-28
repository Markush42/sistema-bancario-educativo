package com.app.banco.banco_educativo_api.application.clientes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear un nuevo Cliente.
    Más adelante transformarla en un record 28/11/25
*/

@Data
@NoArgsConstructor
public class ClienteRequestDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String nombre;

    @NotNull
    @Size(min = 1, max = 50)
    private String apellido;

    @NotNull
    @Size(min = 7, max = 8)
    private String dni;
}
