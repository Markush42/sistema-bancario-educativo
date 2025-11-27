package com.app.banco.banco_educativo_api.application.clientes;

import com.app.banco.banco_educativo_api.application.dto.CuentaBancariaRequestDto;
import com.app.banco.banco_educativo_api.application.dto.CuentaBancariaResponseDto;
import com.app.banco.banco_educativo_api.application.mapper.CuentaBancariaMapper;
import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import com.app.banco.banco_educativo_api.infrastructure.persistence.cuentas.CuentaBancariaRepository;
import com.app.banco.banco_educativo_api.infrastructure.persistence.clientes.ClienteRepository;



import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final ClienteRepository clienteRepository;
    private final CuentaBancariaMapper cuentaBancariaMapper;

    public CuentaBancariaResponseDto crearCuentaBancaria(CuentaBancariaRequestDto requestDto) {
        // Verificar existencia del cliente asociado
        Cliente cliente = clienteRepository.findById(requestDto.getClienteId())
            .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id " + requestDto.getClienteId()));
        // Mapear DTO a entidad de CuentaBancaria (sin asignar cliente aún)
        CuentaBancaria cuenta = cuentaBancariaMapper.toEntity(requestDto);
        // Asignar el cliente existente a la cuenta bancaria y guardar
        cuenta.setCliente(cliente);
        CuentaBancaria cuentaGuardada = cuentaBancariaRepository.save(cuenta);
        // Mapear entidad guardada a DTO de respuesta
        return cuentaBancariaMapper.toResponseDto(cuentaGuardada);
    }

    public CuentaBancariaResponseDto obtenerCuentaBancariaPorId(Long id) {
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("CuentaBancaria no encontrada con id " + id));
        return cuentaBancariaMapper.toResponseDto(cuenta);
    }

    public List<CuentaBancariaResponseDto> listarTodasLasCuentas() {
        List<CuentaBancaria> cuentas = cuentaBancariaRepository.findAll();
        return cuentaBancariaMapper.toResponseDtoList(cuentas);
    }

    public void eliminarCuentaBancariaPorId(Long id) {
        if (!cuentaBancariaRepository.existsById(id)) {
            throw new NoSuchElementException("CuentaBancaria no encontrada con id " + id);
        }
        cuentaBancariaRepository.deleteById(id);
    }
}
