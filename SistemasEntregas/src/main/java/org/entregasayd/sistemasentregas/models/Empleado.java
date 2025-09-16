package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @Column(name = "id_empleado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "codigo_empleado", nullable = false, unique = true, length = 20)
    private String codigoEmpleado;

    @Column(name = "numero_igss", unique = true, length = 20)
    private String numeroIgss;

    @Column(name = "numero_irtra", length = 20)
    private String numeroIrtra;

    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", length = 15)
    private EstadoCivil estadoCivil;

    @Column(name = "numero_dependientes", nullable = false)
    private Integer numeroDependientes = 0;

    @Column(name = "contacto_emergencia_nombre", length = 200)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_empleado", nullable = false)
    private EstadoEmpleado estadoEmpleado = EstadoEmpleado.ACTIVO;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "motivo_salida", columnDefinition = "TEXT")
    private String motivoSalida;

    public enum EstadoCivil {
        SOLTERO,
        CASADO,
        DIVORCIADO,
        VIUDO,
        UNION_LIBRE
    }

    public enum EstadoEmpleado {
        ACTIVO,
        INACTIVO,
        SUSPENDIDO,
        DESPEDIDO,
        RENUNCIADO
    }
}
