package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sucursal")
public class Sucursal {


    @Id
    @Column(name = "id_sucural")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "codigo_sucursal", length = 20, nullable = false)
    private String codigoSucursal;

    @Column(name = "nombre_sucursal", length = 150, nullable = false)
    private String nombreSucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", nullable = false)
    private Direccion direccion;

    @Column(name = "horario_apertura")
    private String horarioApertura;

    @Column(name = "horario_cierre")
    private String horarioCierre;

    @Column(name = "dias_operacion", length = 7)
    private String diasOperacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private Estado estado = Estado.ACTIVA;

    public enum Estado {
        ACTIVA,
        INACTIVA,
        TEMPORAL,
        MANTENIMIENTO
    }

}
