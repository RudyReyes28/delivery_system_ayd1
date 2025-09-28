package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByTipoIncidencia(Incidencia.TipoIncidencia tipoIncidencia);
}
