package org.entregasayd.sistemasentregas.dto.coordinador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleRepartidorDTO {
    private repartidorDetalleDTO repartidor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class repartidorDetalleDTO {
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
        private EmpleadoDTO empleadoDetalle;

        //Detalles del contrato asociado
        private ContratoDTO contratoDetalle;

        //Guias asignadas activas
        private ArrayList<GuiasAsignadasDTO> guiasAsignadas;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmpleadoDTO {
        private Long idEmpleado;
        private String codigoEmpleado;
        private String estadoEmpleado;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContratoDTO {
        private Long idContrato;
        private String tipoContrato;
        private String fechaInicio;
        private String fechaFin;
        private String estadoContrato;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuiasAsignadasDTO {
        private Long idGuia;
        private String numeroGuia;
        private String estadoActual;
        private String fechaCreacion;
        private String descripcionContenido;

    }

}
