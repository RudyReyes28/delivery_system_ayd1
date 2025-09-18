package org.entregasayd.sistemasentregas.dto.contrato;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.Contrato;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoResponseDTO {
    private Long idContrato;
    private Long idEmpleado;
    private String numeroContrato;
    private Contrato.TipoContrato tipoContrato;
    private Contrato.ModalidadTrabajo modalidadTrabajo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean renovacionAutomatica;
    private Double salarioBase;
    private String moneda = "GTQ";
    private Contrato.FrecuenciaPago frecuenciaPago;
    private Boolean incluyeAguinaldo = true;
    private Boolean incluyeBono14 = true;
    private Boolean incluyeVacaciones = true;
    private Boolean incluyeIgss = true;
    private Contrato.EstadoContrato estadoContrato;
}
