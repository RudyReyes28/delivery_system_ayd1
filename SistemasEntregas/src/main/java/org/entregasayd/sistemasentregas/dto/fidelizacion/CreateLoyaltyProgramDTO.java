package org.entregasayd.sistemasentregas.dto.fidelizacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoyaltyProgramDTO {
    //COMO ES UN NUEVO PROGRAMA DE FIDELIZACION SOLO NECESITAREMOS LA EMPRESA Y EL TIPO DE PROGRAMA
    private long idEmpresa;
    private String codigoNivel; //PLATA, ORO, DIAMANTE
}
