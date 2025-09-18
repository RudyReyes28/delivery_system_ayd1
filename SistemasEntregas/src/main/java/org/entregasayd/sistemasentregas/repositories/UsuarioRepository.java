package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Usuario;
import org.entregasayd.sistemasentregas.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNombreUsuario(String username);
    Optional<Usuario> findByPersona(Persona persona);
    ArrayList<Usuario> findByRol_Nombre(String nombreRol);
}
