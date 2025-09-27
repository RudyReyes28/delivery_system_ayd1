package org.entregasayd.sistemasentregas.dto.liquidacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class NuevoPeriodoLiquidacion {
    private String descripcion;
    private String fechaInicio; // Usar String para simplificar la entrada de fechas
    private String fechaFin;    // Usar String para simplificar la entrada de fechas
}

