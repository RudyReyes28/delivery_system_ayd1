package org.entregasayd.sistemasentregas.dto.cancelaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CancelacionGuiaDTO {
    private Long idGuia;
    private String motivoCategoria;
    private String motivoDetalle;
    private Long idUsuario;
}

/*
{
    "idGuia": 1,
    "motivoCategoria": "ERROR_INFORMACION",
    "motivoDetalle": "El cliente proporcionó una dirección incorrecta.",
    "idUsuario": 2
}

 */