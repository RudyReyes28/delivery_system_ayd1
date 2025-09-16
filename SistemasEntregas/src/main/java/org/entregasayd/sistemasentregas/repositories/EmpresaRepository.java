package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
