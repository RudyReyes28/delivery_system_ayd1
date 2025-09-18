package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.contrato.ContratoResponseDTO;
import org.entregasayd.sistemasentregas.models.Contrato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContratoMap {

    @Mapping(source = "empleado.idEmpleado", target = "idEmpleado")
    ContratoResponseDTO toDTO(Contrato contrato);
}
