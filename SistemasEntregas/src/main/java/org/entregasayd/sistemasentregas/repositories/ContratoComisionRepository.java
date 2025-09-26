package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContratoComisionRepository extends CrudRepository<ContratoComision, Long> {
    ContratoComision findByContrato_IdContrato(Long contratoIdContrato);

    Optional<ContratoComision> findByIdContratoComision(Long idContratoComision);
}
