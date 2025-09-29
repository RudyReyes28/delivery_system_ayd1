package org.entregasayd.sistemasentregas.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.dto.coordinador.DetalleGuiaOperacionesDTO;
import org.entregasayd.sistemasentregas.dto.guias.GetDetalleGuiaDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntregasDTO {
    private List<EntregasCanceladasDTO> entregasCanceladas;
    private List<EntregasCompletadasDTO> entregasCompletadas;
    private List<EntregasRechazadasDTO> entregasRechazadas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EntregasCanceladasDTO {
        DetalleGuiaOperacionesDTO entregasCanceladas;
        private int totalEntregasCanceladas;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EntregasCompletadasDTO {
        DetalleGuiaOperacionesDTO entregasCompletadas;
        private int totalEntregasCompletadas;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EntregasRechazadasDTO {
        DetalleGuiaOperacionesDTO entregasRechazadas;
        private String motivoRechazo;
        private String fechaRechazo;
        private int totalEntregasRechazadas;
    }

}
