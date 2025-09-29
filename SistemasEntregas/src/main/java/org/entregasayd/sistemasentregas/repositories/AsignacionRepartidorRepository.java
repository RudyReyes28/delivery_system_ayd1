package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.AsignacionRepartidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRepartidorRepository extends JpaRepository<AsignacionRepartidor, Long> {
    List<AsignacionRepartidor> findByGuia_IdGuiaAndEstadoAsignacion(Long idGuia, AsignacionRepartidor.EstadoAsignacion estadoAsignacion);

    List<AsignacionRepartidor> findByRepartidor_IdRepartidor(Long idRepartidor);

    List<AsignacionRepartidor> findByGuia_IdGuia(Long idGuia);

    List<AsignacionRepartidor> findByRepartidor_IdRepartidorAndGuia_IdGuia(Long idRepartidor, Long idGuia);
}
