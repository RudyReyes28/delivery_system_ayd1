package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.SucursalPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SucursalPersonalRepository extends JpaRepository<SucursalPersonal, Long> {
    ArrayList<SucursalPersonal> findBySucursalIdSucursal(Long idSucursal);
    SucursalPersonal findByUsuarioIdUsuario(Long idUsuario);
}
