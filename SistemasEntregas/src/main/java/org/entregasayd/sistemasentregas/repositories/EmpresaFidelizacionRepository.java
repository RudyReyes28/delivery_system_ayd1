package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Empresa;
import org.entregasayd.sistemasentregas.models.EmpresaFidelizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaFidelizacionRepository extends JpaRepository<EmpresaFidelizacion, Long> {
    boolean existsByEmpresaIdEmpresa(Long idEmpresa);
    boolean existsByEmpresa(Empresa empresa);
    EmpresaFidelizacion findByEmpresaIdEmpresa(Long idEmpresa);
}
