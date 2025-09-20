package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.RepartidorVehiculoDTO;
import org.entregasayd.sistemasentregas.models.RepartidorVehiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepartidorVehiculoMap {

    @Mapping(source = "repartidor.idRepartidor", target = "idRepartidor")
    @Mapping(source = "vehiculo.idVehiculo", target = "idVehiculo")
    RepartidorVehiculoDTO toDTO(RepartidorVehiculo repartidorVehiculo);
}
