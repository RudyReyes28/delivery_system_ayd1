package org.entregasayd.sistemasentregas.dto.repartidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarEstadoDTO {
    private Long idGuia;
    private Long idUsuario;
    private String nuevoEstado;
    private String motivoCambio;
    private String comentarios;

}

/*
Ejemplo de JSON para CambiarEstadoDTO
{
    "idGuia": 1,
    "idUsuario": 4,
    "nuevoEstado": "RECOLECTADA",
    "motivoCambio": "Guia recolectada en la sucursal.",
    "comentarios": "La guia fue recolectada sin problemas."
}
 */