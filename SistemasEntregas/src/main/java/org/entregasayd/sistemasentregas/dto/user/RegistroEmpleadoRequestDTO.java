package org.entregasayd.sistemasentregas.dto.user;

import lombok.Data;

@Data
public class RegistroEmpleadoRequestDTO {
    private UsuarioRequestDdto usuarioRequestDdto;
    private EmpleadoRequestDTO empleadoRequestDdto;
}
