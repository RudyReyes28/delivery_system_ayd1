package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "incidencia")
public class Incidencia {
    @Id
    @Column(name = "id_incidencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;
}
