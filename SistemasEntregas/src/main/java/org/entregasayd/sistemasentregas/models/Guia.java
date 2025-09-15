package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "guia")
public class Guia {
    @Id
    @Column(name = "id_guia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuia;
}
