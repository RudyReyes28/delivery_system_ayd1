package org.entregasayd.sistemasentregas.services.repartidor.evidencia;

import org.entregasayd.sistemasentregas.repositories.EvidenciaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvidenciaService {
    @Autowired
    private EvidenciaEntregaRepository repository;
}
