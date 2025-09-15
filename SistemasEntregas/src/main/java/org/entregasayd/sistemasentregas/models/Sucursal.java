package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sucursal")
public class Sucursal {
    @Id
    @Column(name = "id_sucural")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSucursal;
}
