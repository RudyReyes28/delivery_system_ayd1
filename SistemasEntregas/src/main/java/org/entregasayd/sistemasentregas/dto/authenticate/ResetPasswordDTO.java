package org.entregasayd.sistemasentregas.dto.authenticate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
    private Long idUsuario;
    private String token;
    private String nuevaContrasenia;
}
