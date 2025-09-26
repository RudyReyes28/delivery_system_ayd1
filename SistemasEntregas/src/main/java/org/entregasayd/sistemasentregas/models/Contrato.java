package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contrato")
public class Contrato {
    @Id
    @Column(name = "id_contrato", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(name = "numero_contrato", nullable = false, unique = true, length = 50)
    private String numeroContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad_trabajo", nullable = false)
    private ModalidadTrabajo modalidadTrabajo = ModalidadTrabajo.PRESENCIAL;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "renovacion_automatica", nullable = false)
    private Boolean renovacionAutomatica = false;

    @Column(name = "salario_base", nullable = false)
    private Double salarioBase = 0.00;

    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda = "GTQ";

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_pago", nullable = false, length = 15)
    private FrecuenciaPago frecuenciaPago = FrecuenciaPago.QUINCENAL;

    @Column(name = "incluye_aguinaldo", nullable = false)
    private Boolean incluyeAguinaldo = true;

    @Column(name = "incluye_bono_14", nullable = false)
    private Boolean incluyeBono14 = true;

    @Column(name = "incluye_vacaciones", nullable = false)
    private Boolean incluyeVacaciones = true;

    @Column(name = "incluye_igss", nullable = false)
    private Boolean incluyeIgss = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contrato", nullable = false)
    private EstadoContrato estadoContrato = EstadoContrato.BORRADOR;

    @PrePersist
    private void onCreate(){
        this.estadoContrato = EstadoContrato.BORRADOR;
        this.moneda = "GTQ";
    }

    public enum TipoContrato {
        INDEFINIDO,
        TEMPORAL,
        POR_SERVICIOS,
        PRACTICANTE
    }
    public enum ModalidadTrabajo {
        PRESENCIAL,
        REMOTO,
        HIBRIDO
    }

    public enum FrecuenciaPago {
        SEMANAL,
        QUINCENAL,
        MENSUAL
    }

    public enum EstadoContrato {
        BORRADOR,
        ACTIVO,
        SUSPENDIDO,
        TERMINADO,
        RESCINDIDO
    }
}
