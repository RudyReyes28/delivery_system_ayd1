package org.entregasayd.sistemasentregas.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {
    private Long idUsuario;
    private Long idPersona;
    private Long idRol;
    private String nombreRol;
    private String nombreUsuario;
    private boolean twoFactorEnabled;
    private int intentosFallidos;
    private LocalDateTime ultimaFechaAcceso;
    private Usuario.Estado estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActualizacion;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String dpi;
    private String correo;
    private String telefono;
    private String direccion;
    private String estadoPersona;
}
