package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Guia;
import org.entregasayd.sistemasentregas.models.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByGuiaIdGuia(Long idGuia);

    List<Incidencia> findByTipoIncidencia(Incidencia.TipoIncidencia tipoIncidencia);

    List<Incidencia> findBySeveridad(Incidencia.Severidad severidad);

    List<Incidencia> findByEstado(Incidencia.Estado estado);

    List<Incidencia> findByGuia_IdGuiaAndTipoIncidencia(Long guiaIdGuia, Incidencia.TipoIncidencia tipoIncidencia);
}
