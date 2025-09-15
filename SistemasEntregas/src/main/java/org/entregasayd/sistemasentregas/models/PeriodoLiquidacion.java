package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "periodo_liquidacion")
public class PeriodoLiquidacion {
    @Id
    @Column(name = "id_periodo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;
}
