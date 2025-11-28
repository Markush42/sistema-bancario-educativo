package com.app.banco.banco_educativo_api.application.clientes.dto;

import com.app.banco.banco_educativo_api.domain.clientes.enums.EstadoCliente;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoDocumento;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoPersona;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequestDto(

        @NotBlank
        @Size(max = 20)
        TipoPersona tipoPersona,

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String apellido,

        @NotBlank
        @Size(max = 10)
        TipoDocumento tipoDocumento,

        @NotBlank
        @Size(max = 20)
        String numeroDocumento,

        @Email
        @Size(max = 150)
        String email,

        @Size(max = 20)
        String telefono,

        @Size(max = 255)
        String direccion,

        @NotBlank
        @Size(max = 20)
        EstadoCliente estado
) {}
