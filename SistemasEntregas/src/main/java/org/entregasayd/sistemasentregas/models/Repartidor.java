package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "repartidor")
public class Repartidor {

    @Id
    @Column(name = "id_repartidor", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(name = "numero_licencia", unique = true, length = 50)
    private String numeroLicencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_licencia", nullable = false)
    private TipoLicencia tipoLicencia = TipoLicencia.NINGUNA;

    @Column(name = "fecha_vencimiento_licencia")
    private LocalDate fechaVencimientoLicencia;

    @Column(name = "zona_asignada", length = 100)
    private String zonaAsignada;

    @Column(name = "radio_cobertura_km", nullable = false)
    private Double radioCoberturaKm = 10.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilidad", nullable = false)
    private Disponibilidad disponibilidad = Disponibilidad.DISPONIBLE;

    @Column(name = "calificacion_promedio", nullable = false)
    private Double calificacionPromedio = 0.00;

    @Column(name = "total_entregas_completadas", nullable = false)
    private Integer totalEntregasCompletadas = 0;

    @Column(name = "total_entregas_fallidas", nullable = false)
    private Integer totalEntregasFallidas = 0;

    public enum TipoLicencia {
        A,
        B,
        C,
        M,
        NINGUNA
    }

    public enum Disponibilidad {
        DISPONIBLE,
        OCUPADO,
        DESCANSO,
        INACTIVO
    }
}
