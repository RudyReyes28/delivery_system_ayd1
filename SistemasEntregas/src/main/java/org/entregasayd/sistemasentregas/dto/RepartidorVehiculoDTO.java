package org.entregasayd.sistemasentregas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RepartidorVehiculoDTO {
    private Long idRepartidorVehiculo;
    private Long idRepartidor;
    private Long idVehiculo;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDesasignacion;
    private Boolean esVehiculoPrincipal;
    private Boolean activo;
}
