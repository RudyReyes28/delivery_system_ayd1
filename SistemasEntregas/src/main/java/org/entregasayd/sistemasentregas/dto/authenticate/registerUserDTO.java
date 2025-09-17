package org.entregasayd.sistemasentregas.dto.authenticate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class registerUserDTO {
    //Direccion, Persona, Usuario

    //Direccion
    private String tipoDireccion;
    private String municipio;
    private String departamento;
    private String pais = "Guatemala";
    private String codigoPostal;
    private String referencias;

    //Persona

    private String nombre;
    private String apellido;
    private String fechaNacimiento; // LocalDate
    private String dpi;
    private String correo;
    private String telefono;

    private String nombreUsuario;
    private String contraseniaHash;
    private Long idRol; //Por defecto 2 (Cliente)
    private boolean twoFactorEnable = false;
    private String estado = "ACTIVO"; //Por defecto ACTIVO
}
