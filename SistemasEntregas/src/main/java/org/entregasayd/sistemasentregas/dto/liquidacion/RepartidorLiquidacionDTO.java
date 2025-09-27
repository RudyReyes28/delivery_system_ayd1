package org.entregasayd.sistemasentregas.dto.liquidacion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.dto.coordinador.DetalleRepartidorDTO;
import org.entregasayd.sistemasentregas.models.LiquidacionRepartidor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RepartidorLiquidacionDTO {
    //Aqui vamos a obtener el repartidor con su periodo liquidacion
    private RepartidorDetalleDTO repartidor;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class RepartidorDetalleDTO {
        private Long idRepartidor;
        private String nombreCompleto;
        private String email;
        private String telefono;
        private String zonaAsignada;
        private Double radioCobertura;
        private String disponibilidad;
        private Double calificacionPromedio;
        private Integer totalEntregasCompletadas;
        private Integer totalEntregasFallidas;

        //Detalles del empleado asociado
        private DetalleRepartidorDTO.EmpleadoDTO empleadoDetalle;

        //Detalles del contrato asociado
        private DetalleRepartidorDTO.ContratoDTO contratoDetalle;

        //Periodo de liquidacion
        private LiquidacionRepartidorDTO liquidacionRepartidor;

    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class LiquidacionRepartidorDTO {
        private Long idLiquidacion;
        private Integer totalEntregas;
        private Double totalComisiones;
        private Double totalBonificaciones;
        private Double totalDeducciones;
        private Double subtotal;
        private Double descuentoIgss;
        private Double descuentoIsr;
        private Double otrosDescuentos;
        private Double totalDescuentos;
        private Double totalNeto;
        private String metodoPago;
        private String estado;

        private PeriodoLiquidacionDTO periodoLiquidacion;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class PeriodoLiquidacionDTO {
        private Long idPeriodo;
        private String descripcion;
        private String fechaInicio;
        private String fechaFin;
        private String estado;
        private Double totalComisiones;
        private Double totalBonificaciones;
        private Double totalDeducciones;
        private Double totalNeto;
    }

}
