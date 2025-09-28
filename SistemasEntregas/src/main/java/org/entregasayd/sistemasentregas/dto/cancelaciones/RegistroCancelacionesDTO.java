package org.entregasayd.sistemasentregas.dto.cancelaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RegistroCancelacionesDTO {
    private Long idCancelacion;
    private String motivoCategoria;
    private String motivoDetalle;
    private String fechaCancelacion;
    private String estadoCancelacion;

    //Detalle de la guia
    private GuiaDetalleCancelacionDTO guia;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class GuiaDetalleCancelacionDTO {
        private Long idGuia;
        private String numeroGuia;
        private String codigoInterno;
        private String descripcionContenido;
        private Double valorDeclarado;
        private Double pesoKg;
        private String dimensiones;
        private String observaciones;
        private String fechaCreacion;
        private String fechaRecoleccionReal;
        private String prioridad;
        private Integer intentosEntrega;
        private Double total;
        private String estadoActual;
    }
}
