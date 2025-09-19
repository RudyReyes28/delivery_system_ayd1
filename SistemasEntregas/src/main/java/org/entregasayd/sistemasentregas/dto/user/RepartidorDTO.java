package org.entregasayd.sistemasentregas.dto.user;

import lombok.Data;
import org.entregasayd.sistemasentregas.models.Repartidor;

import java.time.LocalDate;

@Data
public class RepartidorDTO {
    private Long idRepartidor;
    private Long idEmpleado;
    private String numeroLicencia;
    private Repartidor.TipoLicencia tipoLicencia;
    private LocalDate fechaVencimientoLicencia;
    private String zonaAsignada;
    private Double radioCoberturaKm;
    private Repartidor.Disponibilidad disponibilidad;
    private Double calificacionPromedio;
    private Integer totalEntregasCompletadas;
    private Integer totalEntregasFallidas;
}
