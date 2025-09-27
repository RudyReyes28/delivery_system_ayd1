package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Contrato findByIdContrato(Long idContrato);

    Contrato findByNumeroContrato(String numeroContrato);

    List<Contrato> findByEmpleado_IdEmpleado(Long idEmpleado);

    List<Contrato> findByFechaFinBetween(LocalDate fechaFinAfter, LocalDate fechaFinBefore);

   List<Contrato>  findByEstadoContrato(Contrato.EstadoContrato estadoContrato);
   List<Contrato> findByModalidadTrabajo(Contrato.ModalidadTrabajo modalidadTrabajo);
   List<Contrato> findByTipoContrato(Contrato.TipoContrato tipoContrato);

    Contrato findByEmpleado_IdEmpleadoAndEstadoContrato(Long empleadoIdEmpleado, Contrato.EstadoContrato estadoContrato);
}
