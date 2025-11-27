package com.app.banco.banco_educativo_api.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear una nueva CuentaBancaria.
 */
@Data
@NoArgsConstructor
public class CuentaBancariaRequestDto {
    @NotNull
    private Long clienteId;  // Identificador del cliente propietario de la cuenta

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal saldo;  // Saldo inicial de la cuenta (>= 0)
}
