package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.EvidenciaDTO;
import org.entregasayd.sistemasentregas.models.EvidenciaEntrega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EvidenciaMap {

    @Mapping(source = "guia.idGuia", target = "idGuia")
    EvidenciaDTO mapToDTO(EvidenciaEntrega evidencia);
}
