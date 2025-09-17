package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.TokenAutenticacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenAutenticacionRepository extends JpaRepository<TokenAutenticacion, Long> {
    Optional<TokenAutenticacion> findByTokenHashAndTipoToken(String tokenHash, String tipoToken);
    Optional<TokenAutenticacion> findByTokenHash(String tokenHash);
    List<TokenAutenticacion> findByTipoToken(String tipoToken);
    List<TokenAutenticacion> findByEstado(String estado);
}
