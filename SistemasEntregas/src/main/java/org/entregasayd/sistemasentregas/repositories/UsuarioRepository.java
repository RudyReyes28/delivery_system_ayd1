package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
