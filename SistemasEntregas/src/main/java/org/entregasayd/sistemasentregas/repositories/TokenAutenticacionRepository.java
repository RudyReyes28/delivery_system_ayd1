package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.TokenAutenticacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenAutenticacionRepository extends JpaRepository<TokenAutenticacion, Long> {
}
