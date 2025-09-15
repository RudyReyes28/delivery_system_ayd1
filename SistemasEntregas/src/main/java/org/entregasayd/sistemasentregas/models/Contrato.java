package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "contrato")
public class Contrato {
    @Id
    @Column(name = "id_contrato", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;
}
