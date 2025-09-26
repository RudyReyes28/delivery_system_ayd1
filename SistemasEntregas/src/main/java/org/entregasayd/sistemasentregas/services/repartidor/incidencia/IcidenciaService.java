package org.entregasayd.sistemasentregas.services.repartidor.incidencia;

import org.entregasayd.sistemasentregas.repositories.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IcidenciaService {
    @Autowired
    private IncidenciaRepository repository;
}
