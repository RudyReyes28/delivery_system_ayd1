package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cancelacion_guia")
public class CancelacionGuia {
    @Id
    @Column(name = "id_cancelacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCancelacion;
}
