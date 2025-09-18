package org.entregasayd.sistemasentregas.dto.contrato;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.Contrato;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratoRequestDTO {
    private Long idContrato;
    private Long idEmpleado;
    @NotBlank(message = "El número de contrato no puede estar vacío")
    @Size(max = 50, message = "El número de contrato no puede exceder 50 caracteres")
    private String numeroContrato;

    @NotNull(message = "El tipo de contrato no puede ser nulo")
    private Contrato.TipoContrato tipoContrato;

    @NotNull(message = "La modalidad de trabajo no puede ser nula")
    private Contrato.ModalidadTrabajo modalidadTrabajo;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
    private LocalDate fechaInicio;

    @PastOrPresent(message = "La fecha de fin no puede ser futura")
    private LocalDate fechaFin;

    @NotNull(message = "La renovación automática no puede ser nula")
    private Boolean renovacionAutomatica;

    @NotNull(message = "El salario base no puede ser nulo")
    @DecimalMin(value = "0.00", message = "El salario base no puede ser negativo")
    private Double salarioBase;

    @NotNull(message = "La moneda no puede ser nula")
    private String moneda = "GTQ";

    @NotNull(message = "La frecuencia de pago no puede ser nula")
    private Contrato.FrecuenciaPago frecuenciaPago;
    private Boolean incluyeAguinaldo = true;
    private Boolean incluyeBono14 = true;
    private Boolean incluyeVacaciones = true;
    private Boolean incluyeIgss = true;
    private Contrato.EstadoContrato estadoContrato;
}
