package org.entregasayd.sistemasentregas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.Incidencia;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidenciaDTO {
    private Long idIncidencia;

    private Long idGuia;
    private Long idRepartidor;

    private String codigoIncidencia;
    private Incidencia.TipoIncidencia tipoIncidencia;
    private Incidencia.Severidad severidad;

    private String descripcion;
    private String solucionAplicada;

    private Boolean requiereDevolucion;
    private Double costoAdicional;

    private LocalDateTime fechaReporte;
    private LocalDateTime fechaAtencion;
    private LocalDateTime fechaResolucion;

    private Incidencia.Estado estado;
}
