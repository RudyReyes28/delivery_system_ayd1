package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "repartidor_vehiculo")
public class RepartidorVehiculo {
    @Id
    @Column(name = "id_repartidor_vehiculo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidorVehiculo;
}
