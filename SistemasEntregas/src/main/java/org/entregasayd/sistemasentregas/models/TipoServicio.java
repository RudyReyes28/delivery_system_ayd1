package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {
    @Id
    @Column(name = "id_tipo_servicio", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoServicio;
}
