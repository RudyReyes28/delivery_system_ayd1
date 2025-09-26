package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Repartidor;
import org.entregasayd.sistemasentregas.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    List<Repartidor> findRepartidorByTipoLicencia(Repartidor.TipoLicencia tipoLicencia);

    List<Repartidor> findRepartidorByDisponibilidad(Repartidor.Disponibilidad disponibilidad);

    Repartidor findByEmpleado_Usuario_IdUsuario(Long idUsuario);
}
