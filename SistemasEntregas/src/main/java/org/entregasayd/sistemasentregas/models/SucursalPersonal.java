package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "sucursal_personal")
public class SucursalPersonal {
    /*SUCURSAL_PERSONAL
    id_sucursal_personal (PK)
    id_sucursal (FK)
    id_usuario (FK) -- Usuario que trabaja en la sucursal
    cargo VARCHAR(100)
    es_encargado BOOLEAN DEFAULT FALSE
    fecha_inicio DATE DEFAULT (CURRENT_DATE)
    fecha_fin DATE NULL
    activo BOOLEAN DEFAULT TRUE*/

    @Id
    @Column(name = "id_sucursal_personal")
    private Long idSucursalPersonal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "es_encargado", nullable = false)
    private boolean esEncargado = false;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

}
