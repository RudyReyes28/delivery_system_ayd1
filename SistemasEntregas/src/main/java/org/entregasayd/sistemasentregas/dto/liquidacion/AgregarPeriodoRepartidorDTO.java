package org.entregasayd.sistemasentregas.dto.liquidacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AgregarPeriodoRepartidorDTO {
    private Long idRepartidor;
    private Long idPeriodoLiquidacion;
}
/*

{
    "idRepartidor": 1,
    "idPeriodoLiquidacion": 1
}
 */