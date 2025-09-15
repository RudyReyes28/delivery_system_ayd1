package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
