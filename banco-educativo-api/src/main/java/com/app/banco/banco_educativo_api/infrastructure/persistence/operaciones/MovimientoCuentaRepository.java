package com.app.banco.banco_educativo_api.infrastructure.persistence.operaciones;

import com.app.banco.banco_educativo_api.domain.operaciones.MovimientoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad MovimientoCuenta.
 * 
 * Capa: INFRASTRUCTURE
 */
@Repository
public interface MovimientoCuentaRepository extends JpaRepository<MovimientoCuenta, Long> {

    /**
     * Encuentra todos los movimientos de una cuenta bancaria ordenados por fecha
     * descendente.
     */
    List<MovimientoCuenta> findByCuentaBancariaIdOrderByFechaHoraDesc(Long cuentaBancariaId);
}
