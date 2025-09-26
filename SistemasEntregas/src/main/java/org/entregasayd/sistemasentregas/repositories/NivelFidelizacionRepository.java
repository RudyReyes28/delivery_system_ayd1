package org.entregasayd.sistemasentregas.repositories;

import org.entregasayd.sistemasentregas.models.NivelFindelizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NivelFidelizacionRepository extends JpaRepository<NivelFindelizacion, Long> {

    NivelFindelizacion findByCodigoNivel(String codigoNivel);

}
