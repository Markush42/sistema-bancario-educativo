package com.app.banco.banco_educativo_api.infrastructure.persistence.operaciones;

import com.app.banco.banco_educativo_api.domain.operaciones.OperacionFinanciera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad OperacionFinanciera.
 * 
 * Capa: INFRASTRUCTURE
 */
@Repository
public interface OperacionFinancieraRepository extends JpaRepository<OperacionFinanciera, Long> {
}
