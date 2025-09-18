package org.entregasayd.sistemasentregas.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.Empleado;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoRequestDTO {
    private Long idEmpleado;
    private Long idUsuario;
    private String codigoEmpleado;
    private String numeroIgss;
    private String numeroIrtra;
    private String tipoSangre;
    private Empleado.EstadoCivil estadoCivil;
    private Integer numeroDependientes;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private Empleado.EstadoEmpleado estadoEmpleado;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String motivoSalida;
}
