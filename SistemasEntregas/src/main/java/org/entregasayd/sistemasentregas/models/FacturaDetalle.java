package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {
    @Id
    @Column(name = "id_detalle", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;
}
