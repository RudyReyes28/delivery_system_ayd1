package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.EmpresaContacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaContactoRepository extends JpaRepository<EmpresaContacto, Long> {
    //Buscar contacto por ID de empresa
    EmpresaContacto findByEmpresaIdEmpresa(Long idEmpresa);
}
