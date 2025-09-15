package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "evidencia_entrega")
public class EvidenciaEntrega {
    @Id
    @Column(name = "id_evidencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvidenciaEntrega;
}
