package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "factura_empresa")
public class FacturaEmpresa {
    @Id
    @Column(name = "id_factura", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
}
