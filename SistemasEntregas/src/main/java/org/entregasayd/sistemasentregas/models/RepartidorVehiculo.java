package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repartidor_vehiculo")
public class RepartidorVehiculo {
    /*REPARTIDOR_VEHICULO
    id_repartidor_vehiculo (PK)
    id_repartidor (FK)
    id_vehiculo (FK)
    fecha_asignacion DATE DEFAULT (CURRENT_DATE)
    fecha_desasignacion DATE NULL
    es_vehiculo_principal BOOLEAN DEFAULT TRUE
    activo BOOLEAN DEFAULT TRUE
    */
    @Id
    @Column(name = "id_repartidor_vehiculo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidorVehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repartidor", nullable = false)
    private Repartidor repartidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion = LocalDate.now();

    @Column(name = "fecha_desasignacion")
    private LocalDate fechaDesasignacion;

    @Column(name = "es_vehiculo_principal", nullable = false)
    private Boolean esVehiculoPrincipal = true;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
