package org.entregasayd.sistemasentregas.dto.empresas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaDTO {
    private Long idPersona;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String dpi;
    private String correo;
    private String telefono;
    private Long idDireccion;
    private String fechaRegistro;
    private String estado;
}
