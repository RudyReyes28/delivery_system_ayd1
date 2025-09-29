package org.entregasayd.sistemasentregas.dto.reportes;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelacionesEmpresaDTO {
    private Long idEmpresa;
    private String nombreEmpresa;
    private int totalCancelaciones;
    private List<GuiaCancelacionesDTO> guiasCanceladas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GuiaCancelacionesDTO {
        private Long idGuia;
        private String numeroGuia;
        private String motivoCancelacion;
        private String fechaCancelacion;
        private String categoriaCancelacion;
    }

}
