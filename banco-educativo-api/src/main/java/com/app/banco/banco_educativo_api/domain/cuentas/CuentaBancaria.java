package com.app.banco.banco_educativo_api.domain.cuentas;

import com.app.banco.banco_educativo_api.domain.clientes.Cliente;
import com.app.banco.banco_educativo_api.domain.cuentas.enums.EstadoCuenta;
import com.app.banco.banco_educativo_api.domain.cuentas.enums.Moneda;
import com.app.banco.banco_educativo_api.domain.cuentas.enums.TipoCuenta;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "cuentas_bancarias",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_cuenta_numero",
            columnNames = {"numero_cuenta"}
        )
    }
)
public class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, length = 30)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 30)
    private TipoCuenta tipoCuenta; // CAJA_AHORRO / CUENTA_CORRIENTE

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 10)
    private Moneda moneda; // ARS, USD, etc.

    @Column(name = "saldo_actual", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCuenta estado; // ACTIVA / BLOQUEADA / CERRADA

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "cliente_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_cuenta_cliente")
    )
    private Cliente cliente;

    // ==========
    // Constructores
    // ==========

    public CuentaBancaria() {
        // Requerido por JPA
    }

    public CuentaBancaria(
            String numeroCuenta,
            TipoCuenta tipoCuenta,
            Moneda moneda,
            Cliente cliente
    ) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.moneda = moneda;
        this.cliente = cliente;
        this.estado = EstadoCuenta.ACTIVA;
        this.saldoActual = BigDecimal.ZERO;
        this.fechaApertura = LocalDateTime.now();
    }

    // ==========
    // Getters y setters
    // ==========

    public Long getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // ==========
    // Lógica de dominio básica
    // ==========

    public boolean estaActiva() {
        return EstadoCuenta.ACTIVA.equals(this.estado);
    }

    public void bloquear() {
        this.estado = EstadoCuenta.BLOQUEADA;
    }

    public void cerrar() {
        this.estado = EstadoCuenta.CERRADA;
    }

    public void acreditar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a acreditar debe ser positivo");
        }
        this.saldoActual = this.saldoActual.add(monto);
    }

    public void debitar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        }
        if (this.saldoActual.compareTo(monto) < 0) {
            throw new IllegalStateException("Saldo insuficiente para debitar");
        }
        this.saldoActual = this.saldoActual.subtract(monto);
    }

    @PrePersist
    private void prePersist() {
        if (this.estado == null) {
            this.estado = EstadoCuenta.ACTIVA;
        }
        if (this.saldoActual == null) {
            this.saldoActual = BigDecimal.ZERO;
        }
        if (this.fechaApertura == null) {
            this.fechaApertura = LocalDateTime.now();
        }
    }
}
