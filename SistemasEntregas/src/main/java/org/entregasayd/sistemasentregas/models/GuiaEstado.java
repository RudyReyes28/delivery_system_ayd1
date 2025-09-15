package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "guia_estado")
public class GuiaEstado {
    @Id
    @Column(name = "id_guia_estado", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuiaEstado;
}
