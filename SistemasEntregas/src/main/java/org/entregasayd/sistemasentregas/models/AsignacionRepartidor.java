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
@Table(name = "asignacion_repartidor")
public class AsignacionRepartidor {
    @Id
    @Column(name = "id_asignacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia")
    private Guia guia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor")
    private Repartidor repartidor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_asignacion", nullable = false)
    private TipoAsignacion tipoAsignacion = TipoAsignacion.MANUAL; // 'automatica', 'manual', 'reasignacion

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    @Column(name = "fecha_rechazo")
    private LocalDateTime fechaRechazo;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;

    @Column(name = "tiempo_limite_aceptacion")
    private LocalDateTime tiempoLimiteAceptacion;

    @Column(name = "tiempo_estimado_recoleccion")
    private Integer tiempoEstimadoRecoleccion; // En minutos

    @Column(name = "tiempo_estimado_entrega")
    private Integer tiempoEstimadoEntrega; // En minutos

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asignacion", nullable = false)
    private EstadoAsignacion estadoAsignacion = EstadoAsignacion.PENDIENTE; // 'pendiente', 'aceptada', 'rechazada', 'completada', 'cancelada

    public enum TipoAsignacion {
        AUTOMATICA,
        MANUAL,
        REASIGNACION
    }

    public enum EstadoAsignacion {
        PENDIENTE,
        ACEPTADA,
        RECHAZADA,
        COMPLETADA,
        CANCELADA
    }


}
