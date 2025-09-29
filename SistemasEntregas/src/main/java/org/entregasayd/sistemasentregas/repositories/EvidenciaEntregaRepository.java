package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.EvidenciaEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaEntregaRepository extends JpaRepository<EvidenciaEntrega, Long> {

    List<EvidenciaEntrega> findByGuiaIdGuia(Long idGuia);
}
