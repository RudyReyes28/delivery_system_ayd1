package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.user.PersonaDto;
import org.entregasayd.sistemasentregas.mapper.PersonaMap;
import org.entregasayd.sistemasentregas.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    private PersonaService personaService;
    @Autowired
    private PersonaMap personaMap;

    @GetMapping("/get-all")
    public List<PersonaDto> getAll(){
        return personaService.findAll();
    }

    @GetMapping("/get-by-dpi/{dpi}")
    public PersonaDto getByDpi(@PathVariable String dpi){
        return personaMap.toDto(personaService.findByDpi(dpi));
    }

    @GetMapping("/get-by-id/{id}")
    public PersonaDto getById(@PathVariable Long id){
        return personaMap.toDto(personaService.findById(id));
    }
}
