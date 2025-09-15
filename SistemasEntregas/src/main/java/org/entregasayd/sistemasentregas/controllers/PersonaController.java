package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.user.PersonaDTO;
import org.entregasayd.sistemasentregas.models.Persona;
import org.entregasayd.sistemasentregas.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/persona")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/get-all")
    public List<PersonaDTO> getAll(){
        return personaService.findAll();
    }
}
