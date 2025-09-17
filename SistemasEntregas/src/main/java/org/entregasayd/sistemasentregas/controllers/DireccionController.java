package org.entregasayd.sistemasentregas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.entregasayd.sistemasentregas.models.Direccion;
import org.entregasayd.sistemasentregas.services.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/direccion")
public class DireccionController {
    @Autowired
    private DireccionService direccionService;
    @GetMapping("/get-all")
    public List<Direccion> getAll(){
        return direccionService.findAll();
    }

    @PostMapping("/create")
    public Direccion create(@Valid @RequestBody  Direccion direccion){
        return direccionService.create(direccion);
    }

    @PutMapping("/update")
    public Direccion update(@Valid @RequestBody  Direccion direccion){
        return direccionService.update(direccion);
    }
}
