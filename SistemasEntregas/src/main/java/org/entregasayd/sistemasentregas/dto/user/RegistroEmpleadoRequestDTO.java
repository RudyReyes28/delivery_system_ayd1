package org.entregasayd.sistemasentregas.dto.user;

import lombok.Data;
import org.entregasayd.sistemasentregas.dto.contrato.RegisterContratoDTO;
import org.entregasayd.sistemasentregas.models.Direccion;

@Data
public class RegistroEmpleadoRequestDTO {
    private UsuarioRequestDdto usuarioRequestDdto;
    private EmpleadoRequestDTO empleadoRequestDdto;
    private RegisterContratoDTO contrato;
    private Direccion direccion;
}
