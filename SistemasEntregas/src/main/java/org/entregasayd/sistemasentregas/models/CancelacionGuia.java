package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cancelacion_guia")
public class CancelacionGuia {

    @Id
    @Column(name = "id_cancelacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCancelacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    private Guia guia;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_categoria", nullable = false)
    private MotivoCategoria motivoCategoria;

    @Column(name = "motivo_detalle", columnDefinition = "TEXT", nullable = false)
    private String motivoDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelada_por", nullable = false)
    private Usuario canceladaPor;

    @Column(name = "fecha_cancelacion", nullable = false)
    private LocalDateTime fechaCancelacion = LocalDateTime.now();

    @Column(name = "aplica_penalizacion", nullable = false)
    private Boolean aplicaPenalizacion = false;

    @Column(name = "monto_penalizacion", nullable = false)
    private Double montoPenalizacion = 0.00;

    @Column(name = "pagar_comision_repartidor", nullable = false)
    private Boolean pagarComisionRepartidor = false;

    @Column(name = "monto_comision_pagar", nullable = false)
    private Double montoComisionPagar = 0.00;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cancelacion", nullable = false)
    private EstadoCancelacion estadoCancelacion = EstadoCancelacion.SOLICITADA;

    public enum MotivoCategoria {
        ERROR_INFORMACION,
        CAMBIO_CLIENTE,
        PRODUCTO_NO_DISPONIBLE,
        FUERZA_MAYOR,
        DECISION_COMERCIAL,
        OTRO
    }

    public enum EstadoCancelacion {
        SOLICITADA,
        AUTORIZADA,
        PROCESADA,
        RECHAZADA
    }




}
