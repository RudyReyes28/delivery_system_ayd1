package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "liquidacion_repartidor")
public class LiquidacionRepartidor {

    @Id
    @Column(name = "id_liquidacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLiquidacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_liquidacion", nullable = false)
    private PeriodoLiquidacion periodoLiquidacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor", nullable = false)
    private Repartidor repartidor;

    @Column(name = "total_entregas", nullable = false)
    private Integer totalEntregas;

    @Column(name = "total_comisiones", nullable = false)
    private Double totalComisiones;

    @Column(name = "total_bonificaciones", nullable = false)
    private Double totalBonificaciones = 0.00;

    @Column(name = "total_deducciones", nullable = false)
    private Double totalDeducciones = 0.00;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "descuento_igss", nullable = false)
    private Double descuentoIgss = 0.00;

    @Column(name = "descuento_isr", nullable = false)
    private Double descuentoIsr = 0.00;

    @Column(name = "otros_descuentos", nullable = false)
    private Double otrosDescuentos = 0.00;

    @Column(name = "total_descuentos", nullable = false)
    private Double totalDescuentos = 0.00;

    @Column(name = "total_neto", nullable = false)
    private Double totalNeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    public enum MetodoPago {
        EFECTIVO,
        TRANSFERENCIA,
        CHEQUE,
        DEPOSITO
    }

    public enum EstadoPago {
        PENDIENTE,
        PAGADO,
        RECHAZADO
    }
}
