package com.app.banco.banco_educativo_api.application.cuentas.dto;

import java.math.BigDecimal;

/**
 * DTO de salida para devolver datos de CuentaBancaria.
 * Se devuelve al cliente en las respuestas JSON.
 */
public class CuentaBancariaResponseDto {

    private Long id;
    private BigDecimal saldo;
    private Long clienteId;

    public CuentaBancariaResponseDto() {
    }

    public CuentaBancariaResponseDto(Long id, BigDecimal saldo, Long clienteId) {
        this.id = id;
        this.saldo = saldo;
        this.clienteId = clienteId;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
