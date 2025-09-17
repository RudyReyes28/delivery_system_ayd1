package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);

}
