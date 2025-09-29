package org.entregasayd.sistemasentregas.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.dto.liquidacion.RepartidorLiquidacionDTO;
import org.entregasayd.sistemasentregas.dto.repartidor.ComisionRepartidorDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComisionRepartidorReporteDTO {
    private PeriodoLiquidacionReporteDTO periodo;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PeriodoLiquidacionReporteDTO {
        private Long idPeriodo;
        private String descripcion;
        private String fechaInicio;
        private String fechaFin;
        private String estado;

        private List<RepartidoresLiquidacionDTO> repartidores;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RepartidoresLiquidacionDTO {
        private Long idLiquidacion;
        private Double totalComisiones;
        private Double totalBonificaciones;
        private Double totalDeducciones;
        private Double subtotal;
        private Double descuentoIgss;
        private Double descuentoIsr;
        private Double otrosDescuentos;
        private Double totalDescuentos;
        private Double totalNeto;
        private String nombreRepartidor;
        private Long idRepartidor;

        private List<ComisionRepartidorDTO.DetalleComisionDTO> detallesComision;
    }


}
