package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Contrato findByIdContrato(Long idContrato);

    Contrato findByNumeroContrato(String numeroContrato);
}
