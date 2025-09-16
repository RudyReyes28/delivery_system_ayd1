package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodo_liquidacion")
public class PeriodoLiquidacion {

    @Id
    @Column(name = "id_periodo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPeriodo estado = EstadoPeriodo.ABIERTO;

    @Column(name = "total_comisiones", nullable = false)
    private Double totalComisiones = 0.00;

    @Column(name = "total_bonificaciones", nullable = false)
    private Double totalBonificaciones = 0.00;

    @Column(name = "total_deducciones", nullable = false)
    private Double totalDeducciones = 0.00;

    @Column(name = "total_neto", nullable = false)
    private Double totalNeto = 0.00;

    public enum EstadoPeriodo {
        ABIERTO,
        CERRADO,
        LIQUIDADO
    }
}
