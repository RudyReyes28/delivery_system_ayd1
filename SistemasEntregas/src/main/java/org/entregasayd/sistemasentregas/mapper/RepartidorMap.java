package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.user.RepartidorDTO;
import org.entregasayd.sistemasentregas.models.Repartidor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepartidorMap {

    @Mapping(source = "empleado.idEmpleado", target = "idEmpleado")
    RepartidorDTO toDTO(Repartidor repartidor);

}
