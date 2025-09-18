package org.entregasayd.sistemasentregas.dto.contrato;

import lombok.Data;

@Data
public class RegisterContratoDTO {
    private ContratoRequestDTO contrato;
    private ContratoComisionRequestDTO comision;
}
