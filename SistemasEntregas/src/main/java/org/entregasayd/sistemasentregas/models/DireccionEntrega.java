package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "direccion_entrega")
public class DireccionEntrega {
    @Id
    @Column(name = "id_direccion_entrega", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccionEntrega;
}
