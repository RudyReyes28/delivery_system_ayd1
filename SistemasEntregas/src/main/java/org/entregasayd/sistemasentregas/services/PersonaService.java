package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.dto.user.PersonaDto;
import org.entregasayd.sistemasentregas.models.Persona;
import org.entregasayd.sistemasentregas.repositories.PersonaRepository;
import org.entregasayd.sistemasentregas.mapper.PersonaMap;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
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

    public List<PersonaDto> findAll() {
        List<Persona> lista = personaRepository.findAll();
        return lista.stream().map(personaMap::toDto).collect(Collectors.toList());
    }

    public Persona create(Persona persona){
        return personaRepository.save(persona);
    }

    public Persona findById(Long id){
        return personaRepository.findById(id).orElse(null);
    }

    public Persona findByDpi(String dpi){
        return personaRepository.findByDpi(dpi);
    }

    public Persona findByCorreo(String correo){
        return personaRepository.findByCorreo(correo);
    }

    public Persona update(Long id, Persona persona){
        Persona personaUpdate = personaRepository.findById(id).orElse(null);
        if(personaUpdate == null){
            throw new ErrorApi(404,"Persona no encontrada");
        }
        personaUpdate.setNombre(persona.getNombre());
        personaUpdate.setApellido(persona.getApellido());
        personaUpdate.setFechaNacimiento(persona.getFechaNacimiento());
        personaUpdate.setDpi(persona.getDpi());
        personaUpdate.setCorreo(persona.getCorreo());
        personaUpdate.setTelefono(persona.getTelefono());
        personaUpdate.setEstado(persona.getEstado());
        return personaRepository.save(personaUpdate);
    }
}
