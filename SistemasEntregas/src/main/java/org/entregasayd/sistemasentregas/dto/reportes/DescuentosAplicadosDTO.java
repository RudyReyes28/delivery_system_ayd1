package org.entregasayd.sistemasentregas.dto.reportes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DescuentosAplicadosDTO {
    private Long idNivelFidelizacion;
    private String nombreNivel;
    private String codigoNivel;
    private Double porcentajeDescuento;
    private Double totalDescuentosAplicados;
    private Integer totalEmpresasBeneficiadas;

}
