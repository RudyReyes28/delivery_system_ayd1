package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.user.PersonaDTO;
import org.entregasayd.sistemasentregas.models.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaMap {
    // De entidad a DTO
    @Mapping(source = "direccion.idDireccion", target = "idDireccion")
    @Mapping(source = "estado", target = "estado")
    PersonaDTO toDto(Persona persona);

    // De DTO a entidad
    @Mapping(source = "idDireccion", target = "direccion.idDireccion")
    @Mapping(source = "estado", target = "estado")
    Persona toEntity(PersonaDTO dto);
}
