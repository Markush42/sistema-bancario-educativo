package com.app.banco.banco_educativo_api.application.operaciones.exceptions;

/**
 * Excepción lanzada cuando una operación financiera es inválida por reglas de
 * negocio.
 * Por ejemplo: transferir a la misma cuenta, cuentas inactivas, monedas
 * distintas.
 * 
 * Capa: APPLICATION
 */
public class OperacionInvalidaException extends RuntimeException {

    public OperacionInvalidaException(String message) {
        super(message);
    }
}
