package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByCorreo(String correo);
}
