package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Empresa;
import org.entregasayd.sistemasentregas.models.EmpresaFidelizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaFidelizacionRepository extends JpaRepository<EmpresaFidelizacion, Long> {
    boolean existsByEmpresaIdEmpresa(Long idEmpresa);
    boolean existsByEmpresa(Empresa empresa);
    EmpresaFidelizacion findByEmpresaIdEmpresa(Long idEmpresa);

    List<EmpresaFidelizacion> findByNivelFidelizacion_IdNivel(Long nivelFidelizacionIdNivel);
}
