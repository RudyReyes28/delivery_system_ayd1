package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.RepartidorVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepartidorVehiculoRepository extends JpaRepository<RepartidorVehiculo, Long> {

    RepartidorVehiculo getByRepartidor_IdRepartidorAndVehiculo_IdVehiculo(Long idRepartidor, Long idVehiculo);

    List<RepartidorVehiculo> getByRepartidor_IdRepartidor(Long idRepartidor);
}
