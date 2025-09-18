package org.entregasayd.sistemasentregas.dto.sucursales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStateBranchDTO {
    private long idSucursal;
    private String estado;
}
