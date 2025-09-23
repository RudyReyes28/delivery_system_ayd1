package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.models.Rol;
import org.entregasayd.sistemasentregas.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/get-all")
    public List<Rol> getAll(){
        return rolService.findAll();
    }
}
