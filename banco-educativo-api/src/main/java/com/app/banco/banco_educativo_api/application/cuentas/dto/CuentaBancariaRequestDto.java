package com.app.banco.banco_educativo_api.application.cuentas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada para apertura de cuenta bancaria.
 * Se usa en el controlador para recibir el JSON del cliente.
 */
public class CuentaBancariaRequestDto {

    @NotNull
    private Long clienteId;   // id del cliente dueño de la cuenta

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal saldo; // saldo inicial

    public CuentaBancariaRequestDto() {
    }

    public CuentaBancariaRequestDto(Long clienteId, BigDecimal saldo) {
        this.clienteId = clienteId;
        this.saldo = saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
