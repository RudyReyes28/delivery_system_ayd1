package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.models.Rol;
import org.entregasayd.sistemasentregas.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll(){
        return rolRepository.findAll();
    }

    public Rol findById(Long id){
        return rolRepository.findById(id).orElse(null);
    }

}
