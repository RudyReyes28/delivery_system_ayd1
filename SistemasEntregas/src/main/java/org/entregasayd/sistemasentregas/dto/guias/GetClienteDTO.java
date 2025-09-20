package org.entregasayd.sistemasentregas.dto.guias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetClienteDTO {
    private Long idCliente;
    private String codigoCliente;
    private String nombreCompleto;
    private String telefono;
    private String email;
}
