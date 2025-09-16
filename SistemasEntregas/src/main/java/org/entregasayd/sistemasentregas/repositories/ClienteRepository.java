package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
