package org.entregasayd.sistemasentregas.dto.fidelizacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLoyaltyLevelDTO {
    //Id de la empresa
    private Long idEmpresa;
    //Id del nuevo nivel de fidelizacion
    private Long idNivelFidelizacion;
    //Detalles del nivel de fidelizacion
    private Integer entregasMinimas;
    private Integer entregasMaximas;
    private Double descuentoPorcentaje;
    private Integer cancelacionesGratuitasMes;
    private Double porcentajePenalizacion;
}
