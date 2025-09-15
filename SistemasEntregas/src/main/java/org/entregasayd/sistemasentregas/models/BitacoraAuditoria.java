package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bitacora_auditoria")
public class BitacoraAuditoria {
    @Id
    @Column(name = "id_bitacora", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBitacora;
}
