package com.app.banco.banco_educativo_api.application.clientes.exceptions;

/**
 * Excepción de negocio para indicar que ya existe un cliente
 * con el mismo tipo y número de documento.
 */
public class DocumentoDuplicadoException extends RuntimeException {

    public DocumentoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
