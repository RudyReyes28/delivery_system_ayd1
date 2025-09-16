package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "contrato")
public class Contrato {
   /*CONTRATO
    id_contrato (PK)
    id_empleado (FK)
    numero_contrato VARCHAR(50) UNIQUE NOT NULL
    tipo_contrato ENUM('indefinido','temporal','por_servicios','practicante') NOT NULL
    modalidad_trabajo ENUM('presencial','remoto','hibrido') DEFAULT 'presencial'

    -- Vigencia
    fecha_inicio DATE NOT NULL
    fecha_fin DATE NULL
    renovacion_automatica BOOLEAN DEFAULT FALSE

    -- Términos económicos
    salario_base DECIMAL(10,2) DEFAULT 0.00
    moneda VARCHAR(3) DEFAULT 'GTQ'
    frecuencia_pago ENUM('semanal','quincenal','mensual') DEFAULT 'quincenal'

    -- Beneficios
    incluye_aguinaldo BOOLEAN DEFAULT TRUE
    incluye_bono_14 BOOLEAN DEFAULT TRUE
    incluye_vacaciones BOOLEAN DEFAULT TRUE
    incluye_igss BOOLEAN DEFAULT TRUE

    -- Estado del contrato
    estado_contrato ENUM('borrador','activo','suspendido','terminado','rescindido') DEFAULT 'borrador'

*/
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

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contrato", nullable = false)
    private EstadoContrato estadoContrato = EstadoContrato.BORRADOR;

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
