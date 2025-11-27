package com.app.banco.banco_educativo_api.infrastructure.persistence.clientes;

import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.clientes.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente.
 *
 * Pertenece a la capa de infraestructura y expone
 * operaciones de acceso a datos para la capa de aplicación.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por tipo y número de documento.
     */
    Optional<Cliente> findByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento,
                                                            String numeroDocumento);

    /**
     * Verifica si existe un cliente con ese tipo y número de documento.
     */
    boolean existsByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento,
                                                    String numeroDocumento);
}
