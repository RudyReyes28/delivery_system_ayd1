package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "comision_entrega")
public class ComisionEntrega {
    @Id
    @Column(name = "id_comision", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComision;

}
