package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.CancelacionGuia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelacionGuiaRepository extends JpaRepository<CancelacionGuia, Long> {

    List<CancelacionGuia> findByGuia_IdGuiaAndEstadoCancelacion(Long guiaIdGuia, CancelacionGuia.EstadoCancelacion estadoCancelacion);
}
