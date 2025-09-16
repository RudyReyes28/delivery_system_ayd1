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
@Table(name = "incidencia")
public class Incidencia {


    @Id
    @Column(name = "id_incidencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    private Guia guia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor", nullable = false)
    private Repartidor repartidor;

    @Column(name = "codigo_incidencia", nullable = false, unique = true, length = 20)
    private String codigoIncidencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_incidencia", nullable = false)
    private TipoIncidencia tipoIncidencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "severidad", nullable = false)
    private Severidad severidad = Severidad.MEDIA;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "solucion_aplicada", columnDefinition = "TEXT")
    private String solucionAplicada;

    @Column(name = "requiere_devolucion", nullable = false)
    private Boolean requiereDevolucion = false;

    @Column(name = "costo_adicional", nullable = false)
    private Double costoAdicional = 0.00;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte = LocalDateTime.now();

    @Column(name = "fecha_atencion")
    private LocalDateTime fechaAtencion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.ABIERTA;

    public enum TipoIncidencia {
        CLIENTE_AUSENTE,
        DIRECCION_INCORRECTA,
        PAQUETE_DANADO,
        VEHICULO_AVERIADO,
        ACCIDENTE_TRANSITO,
        ZONA_INSEGURA,
        CONDICIONES_CLIMATICAS,
        CLIENTE_RECHAZA,
        OTRO
    }

    public enum Severidad {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }

    public enum Estado {
        ABIERTA,
        EN_ATENCION,
        RESUELTA,
        CERRADA
    }
}
