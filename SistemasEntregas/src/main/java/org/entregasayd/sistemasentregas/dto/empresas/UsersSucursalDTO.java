package org.entregasayd.sistemasentregas.dto.empresas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersSucursalDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private long idPersona;
    private long idRol;
    private String nombreRol;
    private String estado;
}
