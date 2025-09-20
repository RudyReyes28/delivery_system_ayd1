package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.GuiaEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface GuiaEstadoRepository extends JpaRepository<GuiaEstado, Long> {
    ArrayList<GuiaEstado> findByGuiaIdGuiaOrderByFechaCambioDesc(Long idGuia);

}
