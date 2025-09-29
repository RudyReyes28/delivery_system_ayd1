package org.entregasayd.sistemasentregas.dto.cancelaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.dto.guias.GuiaDetalleClienteDTO;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CancelacionesClienteDTO {
    private CancelacionIndicienciaDTO incidencia;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class CancelacionIndicienciaDTO {
        private Long idIncidencia;
        private String codigoIncidencia;
        private String tipoIncidencia;
        private String severidad;
        private String descripcion;
        private String fechaReporte;
        private String estadoIncidencia;
        private GuiaDetalleClienteDTO.GuiaDetalleDto guia;
    }

}
