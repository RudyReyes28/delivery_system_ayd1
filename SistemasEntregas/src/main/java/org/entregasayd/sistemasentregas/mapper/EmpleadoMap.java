package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.user.EmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.models.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoMap {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    EmpleadoRequestDTO toDto(Empleado empleado);

}
