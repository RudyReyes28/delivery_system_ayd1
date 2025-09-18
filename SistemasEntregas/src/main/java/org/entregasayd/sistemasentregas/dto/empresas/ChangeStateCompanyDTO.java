package org.entregasayd.sistemasentregas.dto.empresas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStateCompanyDTO {
    private Long idEmpresa;
    private String nuevoEstado; // Debe ser uno de los valores del enum Empresa.Estado
}
