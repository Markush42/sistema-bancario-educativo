package com.app.banco.banco_educativo_api.application.clientes.dto;

import com.app.banco.banco_educativo_api.domain.clientes.enums.EstadoCliente;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoDocumento;
import com.app.banco.banco_educativo_api.domain.clientes.enums.TipoPersona;

/**
 * DTO de respuesta para la actualización de Clientes.
 * 
 * Se usa para devolver al cliente los datos finales del recurso
 * luego de haber sido actualizado correctamente.
 */
public record ClienteResponseDto(

                Long id,

                TipoPersona tipoPersona,

                String nombre,

                String apellido,

                TipoDocumento tipoDocumento,

                String numeroDocumento,

                String email,

                String telefono,

                String direccion,

                EstadoCliente estado) {
}
