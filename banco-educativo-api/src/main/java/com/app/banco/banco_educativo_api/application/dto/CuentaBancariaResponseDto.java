package com.app.banco.banco_educativo_api.application.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para devolver datos de CuentaBancaria.
 */
@Data
@NoArgsConstructor
public class CuentaBancariaResponseDto {
    private Long id;
    private BigDecimal saldo;
    private Long clienteId;  // Identificador del cliente propietario
}
