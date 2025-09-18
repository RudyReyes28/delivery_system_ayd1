package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContratoMap {

    @Mapping(source = "empleado.idEmpleado", target = "idEmpleado")
    ContratoResponseDTO toDTO(Contrato contrato);
    @Mapping(source = "contrato.idContrato", target = "idContrato")
    ContratoComisionDTO toComisionDTO(ContratoComision contratoComision);
}
