package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.ComisionEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComisionEntregaRepository extends JpaRepository<ComisionEntrega, Long> {

    List<ComisionEntrega> findByRepartidor_IdRepartidorAndPeriodoLiquidacion_IdPeriodo(Long repartidorIdRepartidor, Long periodoLiquidacionIdPeriodo);

}
