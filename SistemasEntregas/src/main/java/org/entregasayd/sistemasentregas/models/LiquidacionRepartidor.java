package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "liquidacion_repartidor")
public class LiquidacionRepartidor {
    @Id
    @Column(name = "id_liquidacion", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLiquidacion;
}
