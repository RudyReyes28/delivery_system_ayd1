package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Guia;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface GuiaRespository extends CrudRepository<Guia, Long> {
    ArrayList<Guia> findBySucursalOrigenIdSucursal(Long sucursal);

    Guia findByNumeroGuia(String numeroGuia);

    ArrayList<Guia> findByRepartidorIsNull();

    ArrayList<Guia> findByRepartidor_IdRepartidor(Long idRepartidor);
}
