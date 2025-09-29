package org.entregasayd.sistemasentregas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.EvidenciaEntrega;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvidenciaDTO {

    private Long idEvidenciaEntrega;

    private Long idGuia;

    private EvidenciaEntrega .TipoEvidencia tipoEvidencia;

    private String nombreArchivo;
    private String urlArchivo;

    private String nombreReceptor;
    private String documentoReceptor;
    private String parentescoReceptor;

    private String observaciones;
}
