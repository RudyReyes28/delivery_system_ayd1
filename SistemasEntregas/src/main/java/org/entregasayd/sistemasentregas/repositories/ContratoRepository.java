package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Contrato findByIdContrato(Long idContrato);

    Contrato findByNumeroContrato(String numeroContrato);

    List<Contrato> findByEmpleado_IdEmpleado(Long idEmpleado);
}
