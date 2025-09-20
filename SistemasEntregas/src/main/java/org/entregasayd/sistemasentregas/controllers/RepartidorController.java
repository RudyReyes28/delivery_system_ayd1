package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.user.RepartidorDTO;
import org.entregasayd.sistemasentregas.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {
    @Autowired
    private RepartidorService repartidorService;

    @PostMapping("/crear-repartidor")
    public RepartidorDTO crearRepartidor(@RequestBody RepartidorDTO repartidorDTO) {
        return repartidorService.save(repartidorDTO);
    }

    @PutMapping("/actualizar-repartidor")
    public RepartidorDTO actualizarRepartidor(@RequestBody RepartidorDTO repartidorDTO) {
        return repartidorService.update(repartidorDTO);
    }

    @GetMapping("/all")
    public List<RepartidorDTO> getRepartidores() {
        return repartidorService.getRepartidores();
    }

    @GetMapping("/find-by-id/{id}")
    public RepartidorDTO getRepartidoresByEmpleado(@PathVariable Long id) {
        return repartidorService.findById(id);
    }

    @GetMapping("/find-by-tipo-licencia/{tipoLicencia}")
    public List<RepartidorDTO> getRepartidoresPorTipoLicencia(@PathVariable String tipoLicencia) {
        return repartidorService.getRepartidoresPorTipoLicencia(tipoLicencia);
    }

    @GetMapping("/find-by-disponibilidad/{disponibilidad}")
    public List<RepartidorDTO> getRepartidoresPorDisponibilidad(@PathVariable String disponibilidad) {
        return repartidorService.getRepartidoresPorDisponibilidad(disponibilidad);
    }
}
