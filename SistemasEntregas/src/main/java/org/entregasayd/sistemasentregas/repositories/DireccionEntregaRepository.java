package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Cliente;
import org.entregasayd.sistemasentregas.models.Direccion;
import org.entregasayd.sistemasentregas.models.DireccionEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionEntregaRepository extends JpaRepository<DireccionEntrega, Long> {
    DireccionEntrega findByClienteIdCliente(Long clienteIdCliente);
    
}
