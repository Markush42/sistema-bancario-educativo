package com.app.banco.banco_educativo_api.application.clientes.dto;

import com.app.banco.banco_educativo_api.domain.clientes.enums.EstadoCliente;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoDocumento;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoPersona;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para crear un nuevo Cliente.
 * Incluye todos los campos necesarios del formulario.
 */
public record ClienteRequestDto(
        @NotNull(message = "El tipo de persona es obligatorio") TipoPersona tipoPersona,

        @NotBlank(message = "El nombre es obligatorio") @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres") String nombre,

        @NotBlank(message = "El apellido es obligatorio") @Size(min = 1, max = 100, message = "El apellido debe tener entre 1 y 100 caracteres") String apellido,

        @NotNull(message = "El tipo de documento es obligatorio") TipoDocumento tipoDocumento,

        @NotBlank(message = "El número de documento es obligatorio") @Size(min = 7, max = 20, message = "El número de documento debe tener entre 7 y 20 caracteres") String numeroDocumento,

        @Email(message = "El email debe ser válido") @Size(max = 150, message = "El email no puede superar los 150 caracteres") String email,

        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres") String telefono,

        @Size(max = 255, message = "La dirección no puede superar los 255 caracteres") String direccion,

        @NotNull(message = "El estado es obligatorio") EstadoCliente estado) {
}
