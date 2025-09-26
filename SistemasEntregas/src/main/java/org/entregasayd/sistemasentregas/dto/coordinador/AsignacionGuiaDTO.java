package org.entregasayd.sistemasentregas.dto.coordinador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionGuiaDTO {
    private Long idGuia;
    private Long idRepartidor;
    private String fechaEntrega; // Formato: "yyyy-MM-dd"
}
