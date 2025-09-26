package org.entregasayd.sistemasentregas.dto.repartidor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RechazoAsignacionDTO {
    String motivoRechazo;
}
