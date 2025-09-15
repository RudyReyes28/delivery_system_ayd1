package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "nivel_fidelizacion")
public class NivelFindelizacion {
    @Id
    @Column(name = "id_nivel", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNivel;
}
