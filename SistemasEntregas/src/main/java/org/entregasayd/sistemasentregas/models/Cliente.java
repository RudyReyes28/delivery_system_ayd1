package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "id_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "codigo_cliente", unique = true, length = 20)
    private String codigoCliente;

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "horario_preferido_inicio")
    private String horarioPreferidoInicio;

    @Column(name = "horario_preferido_fin")
    private String horarioPreferidoFin;

    @Column(name = "instrucciones_entrega", columnDefinition = "TEXT")
    private String instruccionesEntrega;

    @Column(name = "acepta_entregas_vecinos", nullable = false)
    private Boolean aceptaEntregasVecinos = false;

    @Column(name = "requiere_identificacion", nullable = false)
    private Boolean requiereIdentificacion = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "total_entregas_recibidas", nullable = false)
    private Integer totalEntregasRecibidas = 0;

    @Column(name = "total_entregas_rechazadas", nullable = false)
    private Integer totalEntregasRechazadas = 0;

    @Column(name = "calificacion_promedio", nullable = false)
    private Double calificacionPromedio = 0.00;

    public enum Estado {
        ACTIVO,
        INACTIVO;
    }
}
