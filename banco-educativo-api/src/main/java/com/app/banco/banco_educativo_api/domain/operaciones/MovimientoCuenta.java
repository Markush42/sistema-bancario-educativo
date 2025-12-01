package com.app.banco.banco_educativo_api.domain.operaciones;

import com.app.banco.banco_educativo_api.domain.cuentas.CuentaBancaria;
import com.app.banco.banco_educativo_api.domain.operaciones.enums.TipoMovimiento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un movimiento en una cuenta bancaria.
 * 
 * Capa: DOMAIN
 * 
 * Cada movimiento está asociado a:
 * - Una cuenta bancaria (donde se registra el movimiento)
 * - Una operación financiera (que generó este movimiento)
 */
@Entity
@Table(name = "movimientos_cuenta")
public class MovimientoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento; // DEBITO / CREDITO

    @Column(name = "monto", nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Column(name = "saldo_posterior", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoPosterior;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movimiento_cuenta"))
    private CuentaBancaria cuentaBancaria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operacion_financiera_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movimiento_operacion"))
    private OperacionFinanciera operacionFinanciera;

    // ==========
    // Constructores
    // ==========

    public MovimientoCuenta() {
        // Requerido por JPA
    }

    public MovimientoCuenta(
            TipoMovimiento tipoMovimiento,
            BigDecimal monto,
            BigDecimal saldoPosterior,
            CuentaBancaria cuentaBancaria,
            OperacionFinanciera operacionFinanciera,
            String descripcion) {
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.saldoPosterior = saldoPosterior;
        this.cuentaBancaria = cuentaBancaria;
        this.operacionFinanciera = operacionFinanciera;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
    }

    // ==========
    // Getters y setters
    // ==========

    public Long getId() {
        return id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getSaldoPosterior() {
        return saldoPosterior;
    }

    public void setSaldoPosterior(BigDecimal saldoPosterior) {
        this.saldoPosterior = saldoPosterior;
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

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public OperacionFinanciera getOperacionFinanciera() {
        return operacionFinanciera;
    }

    public void setOperacionFinanciera(OperacionFinanciera operacionFinanciera) {
        this.operacionFinanciera = operacionFinanciera;
    }

    @PrePersist
    private void prePersist() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }
}
