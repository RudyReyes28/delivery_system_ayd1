package org.entregasayd.sistemasentregas.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDdto {
    private Long idUsuario;
    private Long idPersona;
    private Long idRol;
    private Long idDireccion;
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String dpi;
    private String correo;
    private String telefono;
    private String estado;
}
