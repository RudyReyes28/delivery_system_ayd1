package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "repartidor")
public class Repartidor {
    @Id
    @Column(name = "id_repartidor", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidor;
}
