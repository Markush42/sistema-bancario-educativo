package com.app.banco.banco_educativo_api.application.cuentas;

import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.cuentas.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.application.cuentas.mapper.CuentaBancariaMapper;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import com.app.banco.banco_educativo_api.infrastructure.persistence.clientes.ClienteRepository;
import com.app.banco.banco_educativo_api.infrastructure.persistence.cuentas.CuentaBancariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de aplicación para cuentas bancarias.
 */
@Service
public class CuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final ClienteRepository clienteRepository;
    private final CuentaBancariaMapper cuentaBancariaMapper;

    public CuentaBancariaService(CuentaBancariaRepository cuentaBancariaRepository,
                                 ClienteRepository clienteRepository,
                                 CuentaBancariaMapper cuentaBancariaMapper) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.clienteRepository = clienteRepository;
        this.cuentaBancariaMapper = cuentaBancariaMapper;
    }

    @Transactional
    public CuentaBancariaResponseDto crearCuentaBancaria(CuentaBancariaRequestDto requestDto) {
        // 1) Buscar cliente
        Cliente cliente = clienteRepository.findById(requestDto.getClienteId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cliente no encontrado con id " + requestDto.getClienteId()
                ));

        // 2) Mapear DTO -> entidad
        CuentaBancaria cuenta = cuentaBancariaMapper.toEntity(requestDto);
        // 3) Asociar cliente
        cuenta.setCliente(cliente);

        // 4) Persistir
        CuentaBancaria guardada = cuentaBancariaRepository.save(cuenta);

        // 5) Mapear entidad -> DTO respuesta
        return cuentaBancariaMapper.toResponseDto(guardada);
    }

    @Transactional(readOnly = true)
    public CuentaBancariaResponseDto obtenerCuentaBancariaPorId(Long id) {
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Cuenta bancaria no encontrada con id " + id
                ));
        return cuentaBancariaMapper.toResponseDto(cuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentaBancariaResponseDto> listarTodasLasCuentas() {
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.findAll();
        return cuentaBancariaMapper.toResponseDtoList(cuentas);
    }

    @Transactional
    public void eliminarCuentaBancariaPorId(Long id) {
        if (!cuentaBancariaRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Cuenta bancaria no encontrada con id " + id
            );
        }
        cuentaBancariaRepository.deleteById(id);
    }
}
