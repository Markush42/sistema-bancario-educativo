package com.app.banco.banco_educativo_api.infrastructure.persistence.cuentas;

import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad CuentaBancaria.
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {

    /**
     * Busca una cuenta por su número único.
     */
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);

    /**
     * Obtiene todas las cuentas de un cliente dado.
     */
    List<CuentaBancaria> findByClienteId(Long clienteId);

    /**
     * Verifica si existe una cuenta con ese número.
     */
    boolean existsByNumeroCuenta(String numeroCuenta);
}
