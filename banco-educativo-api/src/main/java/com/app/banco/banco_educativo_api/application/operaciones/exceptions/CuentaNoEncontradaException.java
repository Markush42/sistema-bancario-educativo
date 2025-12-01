package com.app.banco.banco_educativo_api.application.operaciones.exceptions;

public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException(String message) {
        super(message);
    }
}
