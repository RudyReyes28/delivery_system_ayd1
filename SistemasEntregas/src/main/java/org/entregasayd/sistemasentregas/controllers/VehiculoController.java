package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.models.Vehiculo;
import org.entregasayd.sistemasentregas.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @PostMapping("/create")
    public Vehiculo create(@RequestBody Vehiculo vehiculo){
        return vehiculoService.create(vehiculo);
    }

    @PutMapping("/update/{id}")
    public Vehiculo update(@PathVariable Long id, @RequestBody Vehiculo vehiculo){
        return vehiculoService.update(vehiculo);
    }

    @GetMapping("/get-all")
    public List<Vehiculo> getAll(){
        return vehiculoService.findAll();
    }

    @GetMapping("/get-by-id/{id}")
    public Vehiculo getById(@PathVariable Long id){
        return vehiculoService.findById(id);
    }
}
