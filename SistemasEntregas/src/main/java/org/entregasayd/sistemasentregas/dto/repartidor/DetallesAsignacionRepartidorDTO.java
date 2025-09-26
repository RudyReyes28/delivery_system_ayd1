package org.entregasayd.sistemasentregas.dto.repartidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallesAsignacionRepartidorDTO {
    private AsignacionRepartidorDTO asignacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AsignacionRepartidorDTO {
        private Long idAsignacion;
        private String fechaAsignacion;
        private String estadoAsignacion;
        private GuiaAsignacionDTO guia;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuiaAsignacionDTO {
        private Long idGuia;
        private String numeroGuia;
        private String codigoInterno;
        private String descripcionContenido;
        private Double valorDeclarado;
        private Double pesoKg;
        private String dimensiones;
        private String estadoGuia;
        private GetDetalleGuiaDTO.ClienteDTO cliente;
        private GetDetalleGuiaDTO.DireccionEntregaDTO direccionEntrega;
    }

}
