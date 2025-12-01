package com.app.banco.banco_educativo_api.presentation.clientes;

import com.app.banco.banco_educativo_api.application.clientes.ClienteService;
import com.app.banco.banco_educativo_api.application.clientes.dto.ClienteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador MVC para servir vistas HTML de gestión de clientes.
 * 
 * Capa: PRESENTATION (Web MVC)
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ClienteViewController {

    private final ClienteService clienteService;

    @GetMapping
    public String clientes(Model model) {
        // Obtenemos todos los clientes desde el servicio
        List<ClienteResponseDto> clientes = clienteService.listarTodosLosClientes();

        // Mapeamos a un formato simplificado para la vista
        List<ClienteViewDto> clientesView = clientes.stream()
                .map(this::mapToViewDto)
                .collect(Collectors.toList());

        model.addAttribute("clientes", clientesView);
        model.addAttribute("clienteForm", new ClienteFormDto());
        model.addAttribute("pageTitle", "Gestión de Clientes");

        return "clientes";
    }

    private ClienteViewDto mapToViewDto(ClienteResponseDto dto) {
        ClienteViewDto viewDto = new ClienteViewDto();
        viewDto.setId(dto.id());
        viewDto.setTipoPersona(dto.tipoPersona().name());
        viewDto.setNombreCompleto(dto.nombre() + " " + dto.apellido());
        viewDto.setTipoDocumento(dto.tipoDocumento().name());
        viewDto.setNumeroDocumento(dto.numeroDocumento());
        viewDto.setEmail(dto.email());
        viewDto.setTelefono(dto.telefono());
        viewDto.setDireccion(dto.direccion());
        viewDto.setEstado(dto.estado().name());
        return viewDto;
    }

    // DTO interno para la vista
    public static class ClienteViewDto {
        private Long id;
        private String tipoPersona;
        private String nombreCompleto;
        private String tipoDocumento;
        private String numeroDocumento;
        private String email;
        private String telefono;
        private String direccion;
        private String estado;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTipoPersona() {
            return tipoPersona;
        }

        public void setTipoPersona(String tipoPersona) {
            this.tipoPersona = tipoPersona;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public String getTipoDocumento() {
            return tipoDocumento;
        }

        public void setTipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }

        public String getNumeroDocumento() {
            return numeroDocumento;
        }

        public void setNumeroDocumento(String numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    // DTO para el formulario (vacío, solo para binding)
    public static class ClienteFormDto {
        private Long id;
        private String tipoPersona;
        private String nombreCompleto;
        private String tipoDocumento;
        private String numeroDocumento;
        private String email;
        private String telefono;
        private String direccion;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTipoPersona() {
            return tipoPersona;
        }

        public void setTipoPersona(String tipoPersona) {
            this.tipoPersona = tipoPersona;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public String getTipoDocumento() {
            return tipoDocumento;
        }

        public void setTipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }

        public String getNumeroDocumento() {
            return numeroDocumento;
        }

        public void setNumeroDocumento(String numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }
    }
}
