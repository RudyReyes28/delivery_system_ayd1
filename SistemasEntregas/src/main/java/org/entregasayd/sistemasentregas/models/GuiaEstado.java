package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "guia_estado")
public class GuiaEstado {

    @Id
    @Column(name = "id_guia_estado", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuiaEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    private Guia guia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", nullable = false)
    private Estado estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private Estado estadoNuevo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cambio", nullable = false)
    private Usuario usuarioCambio;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "motivo_cambio", columnDefinition = "TEXT")
    private String motivoCambio;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    public enum Estado {
        CREADA,
        ASIGNADA,
        RECOLECTADA,
        EN_TRANSITO,
        EN_REPARTO,
        ENTREGADA,
        DEVUELTA,
        CANCELADA
    }

}
