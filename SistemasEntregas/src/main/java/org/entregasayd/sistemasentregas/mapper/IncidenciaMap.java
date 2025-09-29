package org.entregasayd.sistemasentregas.mapper;


import org.entregasayd.sistemasentregas.dto.IncidenciaDTO;
import org.entregasayd.sistemasentregas.models.Incidencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidenciaMap {

    @Mapping(source = "guia.idGuia" , target = "idGuia")
    @Mapping(source = "repartidor.idRepartidor", target = "idRepartidor")
    IncidenciaDTO mapToDTO(Incidencia incidencia);
}
