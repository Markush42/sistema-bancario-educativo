package com.app.banco.banco_educativo_api.domain.clientes;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "clientes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_cliente_documento",
            columnNames = {"tipo_documento", "numero_documento"}
        )
    }
)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", nullable = false, length = 20)
    private TipoPersona tipoPersona; // FISICA / JURIDICA

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 80)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 30)
    private String numeroDocumento;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCliente estado; // ACTIVO / BLOQUEADO

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    // ==========
    // Constructores
    // ==========

    protected Cliente() {
        // Requerido por JPA
    }

    public Cliente(
            TipoPersona tipoPersona,
            String nombre,
            String apellido,
            TipoDocumento tipoDocumento,
            String numeroDocumento
    ) {
        this.tipoPersona = tipoPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.estado = EstadoCliente.ACTIVO;
        this.fechaAlta = LocalDateTime.now();
    }

    // ==========
    // Getters y setters
    // ==========

    public Long getId() {
        return id;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
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

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    // ==========
    // Lógica sencilla de dominio
    // ==========

    public boolean estaActivo() {
        return EstadoCliente.ACTIVO.equals(this.estado);
    }

    public void bloquear() {
        this.estado = EstadoCliente.BLOQUEADO;
    }

    public void activar() {
        this.estado = EstadoCliente.ACTIVO;
    }

    @PrePersist
    private void prePersist() {
        if (this.estado == null) {
            this.estado = EstadoCliente.ACTIVO;
        }
        if (this.fechaAlta == null) {
            this.fechaAlta = LocalDateTime.now();
        }
    }
}
