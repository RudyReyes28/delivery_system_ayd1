package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    //Obtener empresa por NIT
    Empresa findByNit(String nit);

    //Verificar si existe una empresa por NIT
    boolean existsByNit(String nit);
}
