package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.PeriodoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodoLiquidacionRepository extends JpaRepository<PeriodoLiquidacion, Long> {
    List<PeriodoLiquidacion> findByEstado(PeriodoLiquidacion.EstadoPeriodo estado);

}
