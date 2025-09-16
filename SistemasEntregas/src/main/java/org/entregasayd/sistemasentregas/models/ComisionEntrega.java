package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comision_entrega")
public class ComisionEntrega {

    @Id
    @Column(name = "id_comision", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    private Guia guia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor", nullable = false)
    private Repartidor repartidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_liquidacion")
    private PeriodoLiquidacion periodoLiquidacion;

    @Column(name = "monto_base_calculo", nullable = false)
    private Double montoBaseCalculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comision", nullable = false)
    private TipoComision tipoComision;

    @Column(name = "valor_comision", nullable = false)
    private Double valorComision;

    @Column(name = "monto_comision", nullable = false)
    private Double montoComision;

    @Column(name = "bonificacion", nullable = false)
    private Double bonificacion = 0.00;

    @Column(name = "deduccion", nullable = false)
    private Double deduccion = 0.00;

    @Column(name = "monto_neto", nullable = false)
    private Double montoNeto;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoComision estado = EstadoComision.CALCULADA;

    public enum TipoComision {
        PORCENTAJE,
        MONTO_FIJO
    }

    public enum EstadoComision {
        CALCULADA,
        LIQUIDADA,
        PAGADA
    }


}
