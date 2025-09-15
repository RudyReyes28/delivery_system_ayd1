package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pago_empresa")
public class PagoEmpresa {
    @Id
    @Column(name = "id_pago", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;
}
