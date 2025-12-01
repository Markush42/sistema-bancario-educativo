package com.app.banco.banco_educativo_api.application.cuentas.dto;

import com.app.banco.banco_educativo_api.domain.cuentas.enums.Moneda;
import com.app.banco.banco_educativo_api.domain.cuentas.enums.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada para apertura de cuenta bancaria.
 * Se usa en el controlador para recibir el JSON del cliente.
 */
public class CuentaBancariaRequestDto {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId; // id del cliente dueño de la cuenta

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta; // CAJA_AHORRO o CUENTA_CORRIENTE

    @NotNull(message = "La moneda es obligatoria")
    private Moneda moneda; // ARS, USD, etc.

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo no puede ser negativo")
    private BigDecimal saldo; // saldo inicial

    public CuentaBancariaRequestDto() {
    }

    public CuentaBancariaRequestDto(Long clienteId, TipoCuenta tipoCuenta, Moneda moneda, BigDecimal saldo) {
        this.clienteId = clienteId;
        this.tipoCuenta = tipoCuenta;
        this.moneda = moneda;
        this.saldo = saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
