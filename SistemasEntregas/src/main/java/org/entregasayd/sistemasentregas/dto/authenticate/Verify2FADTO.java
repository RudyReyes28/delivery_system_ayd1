package org.entregasayd.sistemasentregas.dto.authenticate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verify2FADTO {
    private String token;
    private String codigoVerificacion;
}
