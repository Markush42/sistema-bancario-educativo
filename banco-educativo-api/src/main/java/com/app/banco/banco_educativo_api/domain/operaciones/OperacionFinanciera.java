package com.app.banco.banco_educativo_api.domain.operaciones;

import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoOperacion;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio que representa una operación financiera.
 * 
 * Capa: DOMAIN
 * 
 * Una operación financiera puede ser:
 * - DEPOSITO: solo tiene cuentaDestino
 * - EXTRACCION: solo tiene cuentaOrigen
 * - TRANSFERENCIA_INTERNA: tiene ambas cuentas (origen y destino)
 */
@Entity
@Table(name = "operaciones_financieras")
public class OperacionFinanciera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacion", nullable = false, length = 30)
    private TipoOperacion tipoOperacion;

    @Column(name = "monto_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal montoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_origen_id", foreignKey = @ForeignKey(name = "fk_operacion_cuenta_origen"))
    private CuentaBancaria cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_destino_id", foreignKey = @ForeignKey(name = "fk_operacion_cuenta_destino"))
    private CuentaBancaria cuentaDestino;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "operacionFinanciera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoCuenta> movimientos = new ArrayList<>();

    // ==========
    // Constructores
    // ==========

    public OperacionFinanciera() {
        // Requerido por JPA
    }

    public OperacionFinanciera(
            TipoOperacion tipoOperacion,
            BigDecimal montoTotal,
            CuentaBancaria cuentaOrigen,
            CuentaBancaria cuentaDestino,
            String descripcion) {
        this.tipoOperacion = tipoOperacion;
        this.montoTotal = montoTotal;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
    }

    // ==========
    // Getters y setters
    // ==========

    public Long getId() {
        return id;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public CuentaBancaria getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(CuentaBancaria cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public CuentaBancaria getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(CuentaBancaria cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<MovimientoCuenta> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoCuenta> movimientos) {
        this.movimientos = movimientos;
    }

    // ==========
    // Métodos de dominio
    // ==========

    /**
     * Agrega un movimiento a la operación.
     */
    public void agregarMovimiento(MovimientoCuenta movimiento) {
        movimientos.add(movimiento);
        movimiento.setOperacionFinanciera(this);
    }

    @PrePersist
    private void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }
}
