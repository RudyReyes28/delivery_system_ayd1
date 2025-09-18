package org.entregasayd.sistemasentregas.dto.contrato;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.entregasayd.sistemasentregas.models.ContratoComision;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratoComisionRequestDTO {
    private Integer idContratoComision;
    private Integer idContrato;
    private ContratoComision.TipoComision tipoComision;
    private Double porcentaje;
    private Double montoFijo;
    private LocalDate aplicaDesde;
    private LocalDate aplicaHasta;
    private Boolean activo;
    private Integer minimoEntregasMes;
    private Integer maximoEntregasMes;
    private Double factorMultiplicador;
    private LocalDateTime createdAt;
}
