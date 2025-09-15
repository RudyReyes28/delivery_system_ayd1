package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.user.PersonaDTO;
import org.entregasayd.sistemasentregas.models.Persona;
import org.entregasayd.sistemasentregas.repositories.PersonaRepository;
import org.entregasayd.sistemasentregas.mapper.PersonaMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMap personaMap;

    public List<PersonaDTO> findAll() {
        List<Persona> lista = personaRepository.findAll();
        return lista.stream().map(personaMap::toDto).collect(Collectors.toList());
    }
}
