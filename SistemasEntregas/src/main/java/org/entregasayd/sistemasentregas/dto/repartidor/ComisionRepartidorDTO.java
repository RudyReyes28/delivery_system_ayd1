package org.entregasayd.sistemasentregas.dto.repartidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComisionRepartidorDTO {
    private ComisionLiquidacionDTO comision;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ComisionLiquidacionDTO {
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

        private PeriodoLiquidacionComisionDTO periodoLiquidacion;
        private List<DetalleComisionDTO> detallesComision;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PeriodoLiquidacionComisionDTO {
        private Long idPeriodo;
        private String descripcion;
        private String fechaInicio;
        private String fechaFin;
        private String estado;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetalleComisionDTO {
        private Long idDetalle;
        private String fechaEntrega;
        private String numeroGuia;
        private String tipoComision;
        private Double montoBaseCalculo;
        private Double valorComision;
        private Double montoComision;
        private Double bonificacion;
        private Double deduccion;
        private Double montoNeto;
        private String estado;
    }

}
