package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.models.ContratoComision;
import org.entregasayd.sistemasentregas.repositories.ContratoComisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoComisionService {
    @Autowired
    private ContratoComisionRepository repository;

    public ContratoComision create(ContratoComision contratoComision){
        return repository.save(contratoComision);
    }

    public ContratoComision update(ContratoComision contratoComision){
        return repository.save(contratoComision);
    }

    public List<ContratoComision> findAll(){
        return (List<ContratoComision>) repository.findAll();
    }
}
