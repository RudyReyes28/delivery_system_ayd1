package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.LiquidacionRepartidor;
import org.entregasayd.sistemasentregas.models.PeriodoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquidacionRepartidorRepository extends JpaRepository<LiquidacionRepartidor, Long> {

    LiquidacionRepartidor findByRepartidor_IdRepartidorAndPeriodoLiquidacion_Estado(Long repartidorIdRepartidor, PeriodoLiquidacion.EstadoPeriodo periodoLiquidacionEstado);

}
