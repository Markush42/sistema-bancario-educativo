package com.app.banco.banco_educativo_api.application.operaciones;

import com.app.banco.banco_educativo_api.application.operaciones.dto.*;
import com.app.banco.banco_educativo_api.application.operaciones.exceptions.CuentaNoEncontradaException;
import com.app.banco.banco_educativo_api.application.operaciones.exceptions.OperacionInvalidaException;
import com.app.banco.banco_educativo_api.application.operaciones.exceptions.SaldoInsuficienteException;
import com.app.banco.banco_educativo_api.application.operaciones.mapper.OperacionFinancieraMapper;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import com.app.banco.banco_educativo_api.domain.operaciones.MovimientoCuenta;
import com.app.banco.banco_educativo_api.domain.operaciones.OperacionFinanciera;
import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoMovimiento;
import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoOperacion;
import com.app.banco.banco_educativo_api.infrastructure.persistence.cuentas.CuentaBancariaRepository;
import com.app.banco.banco_educativo_api.infrastructure.persistence.operaciones.OperacionFinancieraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Servicio de aplicación para operaciones financieras.
 * 
 * Capa: APPLICATION
 * Responsabilidad: Orquestar depósitos, extracciones y transferencias.
 */
@Service
public class OperacionFinancieraService {

    private final OperacionFinancieraRepository operacionFinancieraRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final OperacionFinancieraMapper mapper;

    public OperacionFinancieraService(
            OperacionFinancieraRepository operacionFinancieraRepository,
            CuentaBancariaRepository cuentaBancariaRepository,
            OperacionFinancieraMapper mapper) {
        this.operacionFinancieraRepository = operacionFinancieraRepository;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.mapper = mapper;
    }

    /**
     * Registra un depósito en una cuenta bancaria.
     * 
     * POST /api/operaciones/depositos
     */
    /**
     * Registra un depósito en una cuenta bancaria.
     * 
     * POST /api/operaciones/depositos
     */
    @Transactional
    public OperacionResponseDto registrarDeposito(DepositoRequestDto request) {
        // 1. Validar que la cuenta destino existe y está activa
        CuentaBancaria cuentaDestino = cuentaBancariaRepository.findById(request.cuentaDestinoId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cuenta destino no encontrada con id " + request.cuentaDestinoId()));

        if (!cuentaDestino.estaActiva()) {
            throw new OperacionInvalidaException("La cuenta destino no está activa");
        }

        // 2. Crear la operación financiera
        OperacionFinanciera operacion = new OperacionFinanciera(
                TipoOperacion.DEPOSITO,
                request.monto(),
                null, // no hay cuenta origen en un depósito
                cuentaDestino,
                request.descripcion());

        // 3. Acreditar el monto a la cuenta
        cuentaDestino.acreditar(request.monto());

        // 4. Crear el movimiento de la cuenta
        MovimientoCuenta movimiento = new MovimientoCuenta(
                TipoMovimiento.CREDITO,
                request.monto(),
                cuentaDestino.getSaldoActual(), // saldo posterior
                cuentaDestino,
                operacion,
                request.descripcion());

        // 5. Asociar movimiento a la operación
        operacion.agregarMovimiento(movimiento);

        // 6. Persistir operación (cascade ALL persiste también los movimientos)
        OperacionFinanciera operacionGuardada = operacionFinancieraRepository.save(operacion);

        // 7. También persisto la cuenta actualizada
        cuentaBancariaRepository.save(cuentaDestino);

        // 8. Retornar DTO
        return mapper.toOperacionResponseDto(operacionGuardada);
    }

    /**
     * Registra una extracción de una cuenta bancaria.
     * 
     * POST /api/operaciones/extracciones
     */
    @Transactional
    public OperacionResponseDto registrarExtraccion(ExtraccionRequestDto request) {
        // 1. Validar que la cuenta origen existe y está activa
        CuentaBancaria cuentaOrigen = cuentaBancariaRepository.findById(request.cuentaOrigenId())
                .orElseThrow(() -> new CuentaNoEncontradaException(
                        "Cuenta origen no encontrada con id " + request.cuentaOrigenId()));

        if (!cuentaOrigen.estaActiva()) {
            throw new OperacionInvalidaException("La cuenta origen no está activa");
        }

        // 2. Validar saldo suficiente
        if (cuentaOrigen.getSaldoActual().compareTo(request.monto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen");
        }

        // 3. Crear la operación financiera
        OperacionFinanciera operacion = new OperacionFinanciera(
                TipoOperacion.EXTRACCION,
                request.monto(),
                cuentaOrigen,
                null, // no hay cuenta destino en una extracción
                request.descripcion());

        // 4. Debitar el monto de la cuenta
        cuentaOrigen.debitar(request.monto());

        // 5. Crear el movimiento de la cuenta
        MovimientoCuenta movimiento = new MovimientoCuenta(
                TipoMovimiento.DEBITO,
                request.monto(),
                cuentaOrigen.getSaldoActual(), // saldo posterior
                cuentaOrigen,
                operacion,
                request.descripcion());

        // 6. Asociar movimiento a la operación
        operacion.agregarMovimiento(movimiento);

        // 7. Persistir operación
        OperacionFinanciera operacionGuardada = operacionFinancieraRepository.save(operacion);

        // 8. Persistir cuenta actualizada
        cuentaBancariaRepository.save(cuentaOrigen);

        // 9. Retornar DTO
        return mapper.toOperacionResponseDto(operacionGuardada);
    }

    /**
     * Registra una transferencia interna entre dos cuentas del banco.
     * 
     * POST /api/operaciones/transferencias
     */
    @Transactional
    public OperacionResponseDto registrarTransferencia(TransferenciaRequestDto request) {
        // 1. Validar que ambas cuentas existen
        CuentaBancaria cuentaOrigen = cuentaBancariaRepository.findById(request.cuentaOrigenId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cuenta origen no encontrada con id " + request.cuentaOrigenId()));

        CuentaBancaria cuentaDestino = cuentaBancariaRepository.findById(request.cuentaDestinoId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cuenta destino no encontrada con id " + request.cuentaDestinoId()));

        // 2. Validar que ambas cuentas están activas
        if (!cuentaOrigen.estaActiva()) {
            throw new OperacionInvalidaException("La cuenta origen no está activa");
        }
        if (!cuentaDestino.estaActiva()) {
            throw new OperacionInvalidaException("La cuenta destino no está activa");
        }

        // 3. Validar que las cuentas no sean la misma
        if (cuentaOrigen.getId().equals(cuentaDestino.getId())) {
            throw new OperacionInvalidaException("No se puede transferir a la misma cuenta");
        }

        // 4. Validar que ambas cuentas tienen la misma moneda (para MVP)
        if (cuentaOrigen.getMoneda() != cuentaDestino.getMoneda()) {
            throw new OperacionInvalidaException(
                    "Las cuentas deben tener la misma moneda para realizar la transferencia");
        }

        // 5. Validar saldo suficiente
        if (cuentaOrigen.getSaldoActual().compareTo(request.monto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen");
        }

        // 6. Crear la operación financiera
        OperacionFinanciera operacion = new OperacionFinanciera(
                TipoOperacion.TRANSFERENCIA_INTERNA,
                request.monto(),
                cuentaOrigen,
                cuentaDestino,
                request.descripcion());

        // 7. Debitar de cuenta origen
        cuentaOrigen.debitar(request.monto());

        // 8. Crear movimiento de débito
        MovimientoCuenta movimientoDebito = new MovimientoCuenta(
                TipoMovimiento.DEBITO,
                request.monto(),
                cuentaOrigen.getSaldoActual(),
                cuentaOrigen,
                operacion,
                request.descripcion());

        // 9. Acreditar en cuenta destino
        cuentaDestino.acreditar(request.monto());

        // 10. Crear movimiento de crédito
        MovimientoCuenta movimientoCredito = new MovimientoCuenta(
                TipoMovimiento.CREDITO,
                request.monto(),
                cuentaDestino.getSaldoActual(),
                cuentaDestino,
                operacion,
                request.descripcion());

        // 11. Asociar ambos movimientos a la operación
        operacion.agregarMovimiento(movimientoDebito);
        operacion.agregarMovimiento(movimientoCredito);

        // 12. Persistir operación (cascade ALL persiste los movimientos)
        OperacionFinanciera operacionGuardada = operacionFinancieraRepository.save(operacion);

        // 13. Persistir cuentas actualizadas
        cuentaBancariaRepository.save(cuentaOrigen);
        cuentaBancariaRepository.save(cuentaDestino);

        // 14. Retornar DTO
        return mapper.toOperacionResponseDto(operacionGuardada);
    }
}
