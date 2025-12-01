package com.app.banco.banco_educativo_api.application.operaciones.exceptions;

/**
 * Excepción lanzada cuando una cuenta no tiene saldo suficiente para realizar
 * una operación.
 * 
 * Capa: APPLICATION
 */
public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
