package org.entregasayd.sistemasentregas.dto.cancelaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class IncidenciaCancelacionDTO {
    private Long idGuia;
    private Long idUsuario;
    private String descripcion;
}
